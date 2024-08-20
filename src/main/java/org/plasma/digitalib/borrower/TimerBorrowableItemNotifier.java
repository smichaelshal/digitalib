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
    private final Lock lockTimes;
    private final Lock lockMapTimeId;

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
        this.lockTimes = new ReentrantLock();
        this.lockMapTimeId = new ReentrantLock();

        this.fetchAllBorrowedItems();
        this.schedule();
    }

    public final boolean add(@NonNull final T item) {
        try {
            List<Borrowing> borrowings = item.getBorrowings();
            Borrowing lastBorrowing = borrowings.get(borrowings.size() - 1);
            Instant expiredTime = lastBorrowing.getExpiredTime();
            UUID id = item.getId();
            this.lockMapTimeId.lock();
            if (this.mapTimeId.containsKey(expiredTime)) {
                if (this.mapTimeId.get(expiredTime).contains(id)) {
                    this.lockMapTimeId.unlock();
                    return false;
                }
                this.mapTimeId.get(expiredTime).add(id);
            } else {
                List<UUID> ids = new ArrayList<>(Collections.singletonList(id));
                this.mapTimeId.put(expiredTime, ids);
            }
            this.lockMapTimeId.unlock();
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
            this.lockMapTimeId.lock();
            List<UUID> ids = this.mapTimeId.get(expiredTime);
            if (ids == null) {
                log.debug("not found ids match to expired time: {}",
                        expiredTime.toString());
                this.lockMapTimeId.unlock();
                return false;
            }

            Future<?> future = this.mapIdFuture.get(item.getId());
            if (future != null && ids.size() == 1) {
                this.mapIdFuture.remove(item.getId());
                future.cancel(true);
            }
            boolean removeResult = ids.remove(item.getId());
            if (removeResult && ids.isEmpty()) {
                this.mapTimeId.remove(expiredTime);
            }
            this.lockMapTimeId.unlock();
            return removeResult;
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
            log.debug("start schedule");
            Runnable runner = () -> {
                log.debug("start runner");
                Instant currentTime = nextTime.get();
                this.lockMapTimeId.lock();
                List<UUID> ids = this.mapTimeId.get(currentTime);
                if(ids == null) {
                    log.error("ids is null");
                    this.lockMapTimeId.unlock();
                    this.schedule();
                }
                if (!ids.isEmpty()) {
                    List<T> items = this.storage.readAll(new IdsFilter<>(ids));
                    for (T item : items) {
                        if (item == null) {
                            log.debug("item in notify is null");
                            continue;
                        }

                        log.debug("send notify with {}", item);
                        this.consumer.accept(item);
                        this.mapIdFuture.remove(item.getId());
                    }

                    this.mapTimeId.remove(currentTime);
                } else {
                    log.debug("running without items");
                }

                this.lockMapTimeId.unlock();
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
        this.lockTimes.lock();
        if (!this.times.contains(expiredTime)) {
            this.times.add(expiredTime);
            Collections.sort(this.times);
        }
        int idsSize = this.times.size();
        this.lockTimes.unlock();
        if (idsSize == 1) {
            this.schedule();
        }
    }

    private Optional<Instant> getNextScheduledTime() {
        this.lockTimes.lock();
        if (this.times.isEmpty()) {
            this.lockTimes.unlock();
            return Optional.empty();
        }

        Instant currentTime = this.times.remove(0);
        this.lockTimes.unlock();
        return Optional.of(currentTime);
    }
}
