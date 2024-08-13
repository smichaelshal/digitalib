package org.plasma.digitalib.borrower;

import com.sun.jdi.InternalException;
import org.junit.jupiter.api.Test;
import org.plasma.digitalib.dtos.Book;
import org.plasma.digitalib.dtos.BookIdentifier;
import org.plasma.digitalib.dtos.Borrowing;
import org.plasma.digitalib.dtos.User;
import org.plasma.digitalib.storage.FilePersistenterStorage;
import org.plasma.digitalib.storage.Storage;

import java.time.Duration;
import java.nio.file.Path;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Semaphore;

import static org.junit.jupiter.api.Assertions.*;


class TimerBorrowableItemNotifierTest {

    @Test
    public void test() throws InternalException {
        Book book = new Book("genre", "summary", new BookIdentifier("name", "author"));
        Instant expiredTime = Instant.now().plus(3, ChronoUnit.SECONDS);
        User user = new User("1234");

        book.getBorrowings().add(
                new Borrowing(user, Instant.now(), Optional.empty(), expiredTime));

        Storage<Book> storage = new FilePersistenterStorage<Book>(new LinkedList<Book>(), Path.of(""));
        storage.create(book);

        Semaphore lock = new Semaphore(1);
        boolean isLocked = lock.tryAcquire();
        if (!isLocked) {
            throw new InternalException();
        }

        TestConsumer testConsumer = new TestConsumer(expiredTime, book, lock);

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(10);
        TimerBorrowableItemNotifier<Book> timerBorrowableItemNotifier = new TimerBorrowableItemNotifier<>(
                scheduler,
                storage,
                testConsumer);

        timerBorrowableItemNotifier.add(book);
        Duration duration = java.time.Duration.between(Instant.now(), expiredTime);

        try {
            Thread.sleep(duration.toMillis() + 1000);

        } catch (Exception e) {
            System.out.println(e);
        }
        assertTrue(lock.tryAcquire());
    }
}