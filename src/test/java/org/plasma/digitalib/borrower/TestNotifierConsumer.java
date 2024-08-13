package org.plasma.digitalib.borrower;

import lombok.AllArgsConstructor;

import java.time.Instant;
import java.util.concurrent.Semaphore;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;


@AllArgsConstructor
class TestNotifierConsumer<T> implements Consumer<T> {
    private final Instant expectedTime;
    private final T expectedItem;
    private final Semaphore lock;

    public void accept(T item) {
        Instant currentTime = Instant.now();
        lock.release();
        assertEquals(item, this.expectedItem);
        assertEquals(currentTime.getEpochSecond(), this.expectedTime.getEpochSecond());
    }
}