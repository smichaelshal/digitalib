package org.plasma.digitalib.borrower;

import org.plasma.digitalib.dtos.BorrowableItem;
import org.plasma.digitalib.dtos.Borrowing;
import org.plasma.digitalib.filters.BorrowingFilter;
import org.plasma.digitalib.filters.IdFilter;
import org.plasma.digitalib.storage.Storage;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;
import java.util.Optional;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Collections;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

public class TimerBorrowableItemNotifier<T extends BorrowableItem>
        implements BorrowableItemNotifier<T> {
    private final ConcurrentMap<Instant, List<UUID>> mapTimeId;
    private final Storage<T> storage;
    private final Consumer<T> consumer;
    private final ScheduledExecutorService scheduler;
    private final List<Instant> times;
    private final Lock lock;

    public TimerBorrowableItemNotifier(
            final ScheduledExecutorService scheduledExecutorService,
            final Storage<T> itemStorage,
            final Consumer<T> notifyConsumer) {

        this.scheduler = scheduledExecutorService;
        this.storage = itemStorage;
        this.consumer = notifyConsumer;
        this.mapTimeId = new ConcurrentHashMap<>();
        this.times = new LinkedList<>();
        this.lock = new ReentrantLock();

        this.fetchAllExpiredItems();
        this.schedule();
    }

    public final boolean add(final T item) {
        try {
            List<Borrowing> borrowings = item.getBorrowings();
            Borrowing lastBorrowing = borrowings.get(borrowings.size() - 1);
            Instant expiredTime = lastBorrowing.getExpiredTime();
            UUID id = item.getId();

            if (this.mapTimeId.containsKey(expiredTime)) {
                if (this.mapTimeId.get(expiredTime).contains(id)) {
                    return false;
                }
                this.mapTimeId.get(expiredTime).add(id);
            } else {
                List<UUID> ids = new ArrayList<>(Collections.singletonList(id));
                this.mapTimeId.put(expiredTime, ids);
            }
            this.addScheduleTime(expiredTime);
            return true;
        } catch (NullPointerException e) {
            return false;
        }
    }

    public final boolean delete(final T item) {
        try {
            List<Borrowing> borrowings = item.getBorrowings();
            Borrowing lastBorrowing = borrowings.get(borrowings.size() - 1);
            Instant expiredTime = lastBorrowing.getExpiredTime();
            List<UUID> ids = this.mapTimeId.get(expiredTime);
            return ids.remove(item.getId());
        } catch (NullPointerException e) {
            return false;
        }
    }

    private void schedule() {
        Optional<Instant> nextTime = this.getNextScheduleTime();
        if (nextTime.isEmpty()) {
            return;
        }
        Duration duration = Duration.between(Instant.now(), nextTime.get());

        Runnable schedulerTask = () -> {
            Runnable runner = () -> {
                Instant currentTime = nextTime.get();

                List<UUID> ids = this.mapTimeId.get(currentTime);

                if (!ids.isEmpty()) {
                    List<T> items = this.storage.readAll(new IdFilter<>(ids));
                    for (T item : items) {
                        this.consumer.accept(item);
                        System.out.println("running");
                    }
                    this.mapTimeId.remove(currentTime);
                }
                this.schedule();
            };

            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(runner);
            executor.shutdown();
        };

        this.scheduler.schedule(
                schedulerTask,
                duration.toMillis(),
                TimeUnit.MILLISECONDS);
    }

    private void fetchAllExpiredItems() {
        BorrowingFilter<T> borrowingFilter = new BorrowingFilter<>();
        List<T> items = this.storage.readAll(borrowingFilter);
        for (T item : items) {
            this.add(item);
        }
    }


    private void addScheduleTime(final Instant expiredTime) {
        this.lock.lock();
        if (!this.times.contains(expiredTime)) {
            this.times.add(expiredTime);
            Collections.sort(this.times);
        }
        this.lock.unlock();
    }

    private Optional<Instant> getNextScheduleTime() {
        this.lock.lock();
        if (this.times.isEmpty()) {
            this.lock.unlock();
            return Optional.empty();
        }
        Instant currentTime = this.times.remove(0);

        this.lock.unlock();
        return Optional.of(currentTime);
    }
}
