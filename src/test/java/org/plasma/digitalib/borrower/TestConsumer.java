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

//        assertEquals(item, this.expectedItem);
        System.out.println(currentTime.getEpochSecond());
        System.out.println(this.expectedTime.getEpochSecond());
        assertEquals(currentTime.getEpochSecond(), this.expectedTime.getEpochSecond());
    }
}