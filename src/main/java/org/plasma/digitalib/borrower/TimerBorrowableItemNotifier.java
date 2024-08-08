package org.plasma.digitalib.borrower;

import org.plasma.digitalib.dtos.BorrowableItem;
import org.plasma.digitalib.dtos.Borrowing;
import org.plasma.digitalib.filters.BorrwingFilter;
import org.plasma.digitalib.filters.IdFilter;
import org.plasma.digitalib.storage.Storage;

import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;

public class TimerBorrowableItemNotifier<T extends BorrowableItem> implements BorrowableItemNotifier<T> {
    private final ScheduledExecutorService scheduler;
    private final ConcurrentMap<Instant, List<UUID>> mapTimeId;
    private final Storage<T> storage;
    private final Consumer<T> consumer;
    private final Runnable schedulerTask;


    public TimerBorrowableItemNotifier(
            ScheduledExecutorService scheduler,
            Storage<T> storage,
            Consumer<T> consumer, Function<T, Boolean> filter) {

        this.scheduler = scheduler;
        this.mapTimeId = new ConcurrentHashMap<>();
        this.storage = storage;
        this.consumer = consumer;


        this.schedulerTask = () -> {
            Instant currentTime = Instant.now();
            List<UUID> ids = this.mapTimeId.get(currentTime);
            if (ids != null) {
                List<T> items = this.storage.readAll(new IdFilter<>(ids));
                for (T item : items) {
                    consumer.accept(item);
                }
                this.mapTimeId.remove(currentTime);
            }
        };

        this.fetchAllExpiredItems();
        scheduler.schedule(schedulerTask, 1, TimeUnit.SECONDS);
    }

    private void fetchAllExpiredItems() {
        BorrwingFilter<T> borrwingFilter = new BorrwingFilter<>();
        List<T> items = this.storage.readAll(borrwingFilter);
        for (T item : items) {
            this.add(item);
        }
    }


    public boolean add(T item) {
        try {
            Borrowing lastBorrowing = item.getBorrowings().getLast();
            Instant expiredTime = lastBorrowing.getExpiredTime();
            UUID id = item.getId();

            if (this.mapTimeId.containsKey(expiredTime)) {
                this.mapTimeId.get(expiredTime).add(id);
            } else {
                List<UUID> ids = new LinkedList<>();
                ids.add(id);
                this.mapTimeId.put(expiredTime, ids);
            }
            return true;
        } catch (NullPointerException e) {
            return false;
        }
    }

    public boolean delete(T item) {
        try {
            Borrowing lastBorrowing = item.getBorrowings().getLast();
            Instant expiredTime = lastBorrowing.getExpiredTime();
            List<UUID> ids = this.mapTimeId.get(expiredTime);
            return ids.remove(item.getId());
        } catch (NullPointerException e) {
            return false;
        }
    }
}
