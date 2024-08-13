package org.plasma.digitalib.borrower;

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

import static org.junit.jupiter.api.Assertions.assertTimeout;


class TimerBorrowableItemNotifierTest {

    @Test
    public void test() {
        Book book = new Book("genre", "summary", new BookIdentifier("name", "author"));
        Instant expiredTime = Instant.now().plus(3, ChronoUnit.SECONDS);
        System.out.println("expiredTime: " + expiredTime.getEpochSecond());
        User user = new User("1234");

        book.getBorrowings().add(
                new Borrowing(user, Instant.now(), Optional.empty(), expiredTime));



        Storage<Book> storage = new FilePersistenterStorage<Book>(new LinkedList<Book>(), Path.of(""));
        storage.create(book);


        Consumer<Book> mockConsumer = Mockito.mock(Consumer.class);

        Semaphore lock = new Semaphore(1);
        boolean isLocked = lock.tryAcquire();


        TestConsumer testConsumer = new TestConsumer(expiredTime, book, lock);

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(10);
        TimerBorrowableItemNotifier<Book> timerBorrowableItemNotifier = new TimerBorrowableItemNotifier<>(
                scheduler,
                storage,
                testConsumer);

        timerBorrowableItemNotifier.add(book);
        Duration duration = java.time.Duration.between(Instant.now(), expiredTime);

        try {
            Thread.sleep(duration.toMillis());

        } catch (Exception e) {
            System.out.println(e);
        }

//        Mockito.verify(mockConsumer, Mockito.times(1)).accept(book);



    }
}