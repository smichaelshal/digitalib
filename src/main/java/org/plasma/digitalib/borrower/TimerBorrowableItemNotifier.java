package org.plasma.digitalib.borrower;

import org.plasma.digitalib.dtos.BorrowableItem;
import org.plasma.digitalib.dtos.Borrowing;
import org.plasma.digitalib.filters.BorrowingFilter;
import org.plasma.digitalib.filters.IdFilter;
import org.plasma.digitalib.storage.Storage;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.Consumer;

public class TimerBorrowableItemNotifier<T extends BorrowableItem> implements BorrowableItemNotifier<T> {
    private final ConcurrentMap<Long, List<UUID>> mapTimeId;
    private final Storage<T> storage;
    private final Consumer<T> consumer;
    private final ScheduledExecutorService scheduler;
    private final Runnable schedulerTask;

    public TimerBorrowableItemNotifier(
            ScheduledExecutorService scheduler,
            Storage<T> storage,
            Consumer<T> consumer) {

        this.scheduler = scheduler;
        this.mapTimeId = new ConcurrentHashMap<>();
        this.storage = storage;
        this.consumer = consumer;

        Runnable runner = () -> {
            long currentTime = Instant.now().getEpochSecond();

            List<UUID> ids = this.mapTimeId.get(currentTime);
            if (ids != null) {
                List<T> items = this.storage.readAll(new IdFilter<>(ids));
                for (T item : items) {
                    this.consumer.accept(item);
                }
                this.mapTimeId.remove(currentTime);
            }
        };

        this.schedulerTask = () -> {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(runner);
        };

        this.fetchAllExpiredItems();
        this.scheduler.scheduleAtFixedRate(this.schedulerTask, 500, 500, TimeUnit.MILLISECONDS);
    }

    private void fetchAllExpiredItems() {
        BorrowingFilter<T> borrowingFilter = new BorrowingFilter<>();
        List<T> items = this.storage.readAll(borrowingFilter);
        for (T item : items) {
            this.add(item);
        }
    }

    public boolean add(T item) {
        try {
            List<Borrowing> borrowings = item.getBorrowings();
            Borrowing lastBorrowing = borrowings.get(borrowings.size() - 1);
            long expiredTime = lastBorrowing.getExpiredTime().getEpochSecond();
            UUID id = item.getId();

            if (this.mapTimeId.containsKey(expiredTime)) {
                this.mapTimeId.get(expiredTime).add(id);
            } else {
                List<UUID> ids = new ArrayList<>(Collections.singletonList(id));
                this.mapTimeId.put(expiredTime, ids);
            }
            return true;
        } catch (NullPointerException e) {
            return false;
        }
    }

    public boolean delete(T item) {
        try {
            List<Borrowing> borrowings = item.getBorrowings();
            Borrowing lastBorrowing = borrowings.get(borrowings.size() - 1);
            long expiredTime = lastBorrowing.getExpiredTime().getEpochSecond();
            List<UUID> ids = this.mapTimeId.get(expiredTime);
            return ids.remove(item.getId());
        } catch (NullPointerException e) {
            return false;
        }
    }
}
