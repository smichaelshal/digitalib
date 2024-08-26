package org.plasma.digitalib.borrower;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.plasma.digitalib.filters.BorrowingFilter;
import org.plasma.digitalib.filters.IdsFilter;
import org.plasma.digitalib.models.BorrowableItem;
import org.plasma.digitalib.models.Borrowing;
import org.plasma.digitalib.storage.Storage;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@Slf4j
public class TimerBorrowableItemNotifier<T extends BorrowableItem> implements
        BorrowableItemNotifier<T> {
    private final ConcurrentMap<Instant, List<UUID>> mapTimeId;
    private final ConcurrentMap<UUID, Future<?>> mapIdFuture;

    private final Storage<T> storage;
    private final Consumer<T> consumer;
    private final ScheduledExecutorService scheduler;

    public TimerBorrowableItemNotifier(
            @NonNull final ScheduledExecutorService scheduledExecutorService,
            @NonNull final Storage<T> itemStorage,
            @NonNull final Consumer<T> notifyConsumer) {
        this.scheduler = scheduledExecutorService;
        this.storage = itemStorage;
        this.consumer = notifyConsumer;
        this.mapTimeId = new ConcurrentHashMap<>();
        this.mapIdFuture = new ConcurrentHashMap<>();
        this.fetchAllBorrowedItems();
    }

    public final boolean add(@NonNull final T item) {
        try {
            List<Borrowing> borrowings = item.getBorrowings();
            Borrowing lastBorrowing = borrowings.get(borrowings.size() - 1);
            Instant expiredTime = lastBorrowing.getExpiredTime();
            UUID id = item.getId();
            Optional<Instant> oldMinTime = Optional.empty();

            synchronized (this.mapTimeId) {
                Set<Instant> allTimes = this.mapTimeId.keySet();
                if (!allTimes.isEmpty()) {
                    oldMinTime = Optional
                            .of(Collections.min(allTimes));
                }

                if (this.mapTimeId.containsKey(expiredTime)) {
                    if (this.mapTimeId.get(expiredTime).contains(id)) {
                        log.debug("Item with same id already exist: {}", item);
                        return false;
                    }

                    this.mapTimeId.get(expiredTime).add(id);
                } else {
                    List<UUID> ids =
                            new ArrayList<>(Collections.singletonList(id));
                    this.mapTimeId.put(expiredTime, ids);
                    log.debug(
                            "Add item to mapTimeId at new schedule time: {} {}",
                            expiredTime, item);
                }
            }

            this.addTimeToSchedule(expiredTime, oldMinTime);
            return true;
        } catch (Exception e) {
            log.error("Failed to add item: {}", item, e);
            return false;
        }
    }

    public final boolean delete(@NonNull final T item) {
        try {
            List<Borrowing> borrowings = item.getBorrowings();
            Borrowing lastBorrowing = borrowings.get(borrowings.size() - 1);
            Instant expiredTime = lastBorrowing.getExpiredTime();
            Instant currentTime = Instant.now();
            synchronized (this.mapTimeId) {
                List<UUID> ids = this.mapTimeId.get(expiredTime);
                if (ids == null) {
                    if (expiredTime.isBefore(currentTime)) {
                        log.debug("Not found ids match to expired time because"
                                        + " the expired time passed: {}",
                                expiredTime);
                        return true;
                    }

                    log.debug("Not found ids match to expired time: {}",
                            expiredTime);
                    return false;
                }

                Future<?> future = this.mapIdFuture.get(item.getId());
                if (future != null && ids.size() == 1) {
                    this.mapIdFuture.remove(item.getId());
                    future.cancel(true);
                }

                boolean removeResult = ids.remove(item.getId());
                if (removeResult) {
                    log.debug("Delete item success {}", item);
                    if (ids.isEmpty()) {
                        log.debug(
                                "The item is last from this "
                                        + "expired time: {} {}",
                                expiredTime, item);
                        this.mapTimeId.remove(expiredTime);
                    }
                } else {
                    log.debug("Delete item failed {}", item);
                }

                return removeResult;
            }

        } catch (Exception e) {
            log.error("Failed to delete item: {}", item, e);
            return false;
        }
    }

    private void runConsumer(final Instant currentTime) {
        log.debug("Start run consumer the current time: {}", currentTime);
        synchronized (this.mapTimeId) {
            List<UUID> ids = this.mapTimeId.get(currentTime);
            if (ids == null) {
                log.error("Ids is null to currentTime: {}",
                        currentTime);
            } else {

                if (!ids.isEmpty()) {
                    List<T> items =
                            this.storage.readAll(new IdsFilter<>(ids));
                    for (T item : items) {
                        if (item == null) {
                            log.debug("Item in notify is null");
                            continue;
                        }

                        log.debug("Send notify with {}", item);
                        this.consumer.accept(item);
                        this.mapIdFuture.remove(item.getId());
                    }
                    this.mapTimeId.remove(currentTime);
                } else {
                    log.debug("Running without items");
                }
            }
        }

        this.schedule();
    }

    private void dispatchRunner(final Instant nextTime) {
        log.debug("Start schedule the next time: {}", nextTime);
        Runnable runner = () -> {
            this.runConsumer(nextTime);
        };

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(runner);
        executor.shutdown();
    }

    private void schedule() {
        Optional<Instant> nextTime = this.getNextScheduledTime();
        if (nextTime.isEmpty()) {
            return;
        }

        Duration duration = Duration.between(Instant.now(), nextTime.get());
        Runnable schedulerTask = () -> {
            this.dispatchRunner(nextTime.get());
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

    private boolean isItemNotExpired(final T item) {
        List<Borrowing> borrowings = item.getBorrowings();
        if (borrowings.isEmpty()) {
            return false;
        }

        Borrowing lastBorrowing = borrowings.get(borrowings.size() - 1);
        return lastBorrowing.getExpiredTime().isAfter(Instant.now());
    }

    private boolean isItemBorrowed(final T item) {
        return item.getIsBorrowed();
    }

    private void fetchAllBorrowedItems() {
        BorrowingFilter<T> borrowingFilter = new BorrowingFilter<>();
        List<T> items = this.storage.readAll(borrowingFilter)
                .stream()
                .filter(this::isItemBorrowed)
                .filter(this::isItemNotExpired)
                .toList();
        for (T item : items) {
            this.add(item);
        }
    }

    private void addTimeToSchedule(
            final Instant newTime,
            final Optional<Instant> oldMinTime) {
        if (oldMinTime.isEmpty() && this.mapIdFuture.isEmpty()) {
            this.schedule();
            return;
        }

        Instant oldTime = oldMinTime.get();
        List<UUID> ids = this.mapTimeId.get(oldTime);
        if (ids == null || ids.isEmpty()) {
            this.schedule();
            return;
        }

        if (oldTime.isAfter(newTime)) {
            for (UUID id : ids) {
                Future<?> future = this.mapIdFuture.get(id);
                if (future != null) {
                    future.cancel(true);
                    this.schedule();
                    return;
                }
            }
        }
    }

    private Optional<Instant> getNextScheduledTime() {
        Instant currentTime;
        synchronized (this.mapTimeId) {
            if (this.mapTimeId.isEmpty()) {
                return Optional.empty();
            }

            currentTime = Collections.min(this.mapTimeId.keySet());
        }

        return Optional.of(currentTime);
    }
}
