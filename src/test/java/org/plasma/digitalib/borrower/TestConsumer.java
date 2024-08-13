package org.plasma.digitalib.borrower;

import lombok.AllArgsConstructor;
import org.plasma.digitalib.dtos.Book;

import java.time.Instant;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;


@AllArgsConstructor
class TestConsumer<T> implements Consumer<T> {
    private final Instant expectedTime;
    private final T expectedItem;
//    private final Consumer<T> consumer;
    private final Semaphore lock;

    public void accept(T item) {
        Instant currentTime = Instant.now();
        lock.release();

        System.out.println("run - currentTime: " + currentTime.getEpochSecond());
        System.out.println("run - expectedTime: " + this.expectedTime.getEpochSecond());
        assertEquals(item, this.expectedItem);
        assertEquals(currentTime.getEpochSecond(), this.expectedTime.getEpochSecond());
    }
}