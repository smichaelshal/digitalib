package org.plasma.digitalib.borrower;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.plasma.digitalib.models.BorrowableItem;
import org.plasma.digitalib.models.Borrowing;
import org.plasma.digitalib.filters.BorrowingFilter;
import org.plasma.digitalib.filters.IdsFilter;
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
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

@Slf4j
public class TimerBorrowableItemNotifier<T extends BorrowableItem>
        implements BorrowableItemNotifier<T> {
    private final ConcurrentMap<Instant, List<UUID>> mapTimeId;
    private final ConcurrentMap<UUID, Future<?>> mapIdFuture;

    private final Storage<T> storage;
    private final Consumer<T> consumer;
    private final ScheduledExecutorService scheduler;
    private final List<Instant> times;
    private final Lock lock;

    public TimerBorrowableItemNotifier(
            @NonNull final ScheduledExecutorService scheduledExecutorService,
            @NonNull final Storage<T> itemStorage,
            @NonNull final Consumer<T> notifyConsumer) {

        this.scheduler = scheduledExecutorService;
        this.storage = itemStorage;
        this.consumer = notifyConsumer;
        this.mapTimeId = new ConcurrentHashMap<>();
        this.mapIdFuture = new ConcurrentHashMap<>();
        this.times = new LinkedList<>();
        this.lock = new ReentrantLock();

        this.fetchAllBorrowedItems();
        this.schedule();
    }

    public final boolean add(@NonNull final T item) {
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
            this.addTimeToSchedule(expiredTime);
            return true;
        } catch (NullPointerException e) {
            log.error(e.toString());
            return false;
        }
    }

    public final boolean delete(@NonNull final T item) {
        try {
            List<Borrowing> borrowings = item.getBorrowings();
            Borrowing lastBorrowing = borrowings.get(borrowings.size() - 1);
            Instant expiredTime = lastBorrowing.getExpiredTime();
            Future<?> future = this.mapIdFuture.get(item.getId());
            if (future != null) {
                this.mapIdFuture.remove(item.getId());
                future.cancel(true);
            }

            List<UUID> ids = this.mapTimeId.get(expiredTime);
            if (ids == null) {
                log.info("not found ids match to expired time: {}",
                        expiredTime.toString());
                return false;
            }
            return ids.remove(item.getId());
        } catch (NullPointerException e) {
            log.error(e.toString());
            return false;
        }
    }

    private void schedule() {
        Optional<Instant> nextTime = this.getNextScheduledTime();
        if (nextTime.isEmpty()) {
            return;
        }

        Duration duration = Duration.between(Instant.now(), nextTime.get());
        Runnable schedulerTask = () -> {
            log.info("start schedule");
            Runnable runner = () -> {
                log.info("start runner");
                Instant currentTime = nextTime.get();
                List<UUID> ids = this.mapTimeId.get(currentTime);
                if (!ids.isEmpty()) {
                    List<T> items = this.storage.readAll(new IdsFilter<>(ids));
                    for (T item : items) {
                        log.info("send notify with {}", item);
                        this.consumer.accept(item);
                        this.mapIdFuture.remove(item.getId());
                    }

                    this.mapTimeId.remove(currentTime);
                } else {
                    log.info("running without items");
                }

                this.schedule();
            };

            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(runner);
            executor.shutdown();
        };

        ScheduledFuture<?> future = this.scheduler.schedule(
                schedulerTask,
                duration.toMillis(),
                TimeUnit.MILLISECONDS);

        List<UUID> ids = this.mapTimeId.get(nextTime.get());
        for (UUID id : ids) {
            this.mapIdFuture.put(id, future);
        }
    }

    private void fetchAllBorrowedItems() {
        BorrowingFilter<T> borrowingFilter = new BorrowingFilter<>();
        List<T> items = this.storage.readAll(borrowingFilter);
        for (T item : items) {
            this.add(item);
        }
    }


    private void addTimeToSchedule(final Instant expiredTime) {
        this.lock.lock();
        if (!this.times.contains(expiredTime)) {
            this.times.add(expiredTime);
            Collections.sort(this.times);
        }

        if (this.times.size() == 1) {
            this.schedule();
        }
        this.lock.unlock();
    }

    private Optional<Instant> getNextScheduledTime() {
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
