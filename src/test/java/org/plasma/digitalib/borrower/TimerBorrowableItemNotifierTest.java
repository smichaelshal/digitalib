package org.plasma.digitalib.borrower;

import com.sun.jdi.InternalException;
import org.junit.jupiter.api.Test;
import org.plasma.digitalib.dtos.Book;
import org.plasma.digitalib.dtos.BookIdentifier;
import org.plasma.digitalib.dtos.Borrowing;
import org.plasma.digitalib.dtos.User;
import org.plasma.digitalib.storage.FilePersistenterStorage;
import org.plasma.digitalib.storage.Storage;

import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;
import java.nio.file.Path;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Semaphore;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class TimerBorrowableItemNotifierTest {

    private Book book;
    private TimerBorrowableItemNotifier<Book> notifier;

    public void setup() {
        Storage<Book> storage = mock(Storage.class);
        Function<Book, Boolean> filter = mock(Function.class);
        this.book = new Book(
                "genre",
                "summary",
                new BookIdentifier("name", "author"));
        List<Book> books = List.of(this.book);

        when(storage.readAll(filter)).thenReturn(books);

//        TestNotifierConsumer<Book> testNotifierConsumer = new TestNotifierConsumer<>(expiredTime, book, lock);

//        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(10);
//
//        this.notifier = new TimerBorrowableItemNotifier<>(
//                scheduler,
//                storage,
//                testNotifierConsumer);

//        when(entityManager.find(Customer.class,1L)).thenReturn(sampleCustomer);
    }

    @Test
    public void add_notify_with_book_should_call_with_book() {


    }

    @Test
    public void notifyTest() throws InternalException, IOException {
        Book book = new Book(
                "genre",
                "summary",
                new BookIdentifier("name", "author"),
                true);
        Instant expiredTime = Instant.now().plus(3, ChronoUnit.SECONDS);
        User user = new User("1234");

        book.getBorrowings().add(
                new Borrowing(user, Instant.now(), Optional.empty(), expiredTime));

        Storage<Book> storage = this.createStorage();
        storage.create(book);

        Semaphore lock = new Semaphore(1);
        boolean isLocked = lock.tryAcquire();
        if (!isLocked) {
            throw new InternalException();
        }

        TestNotifierConsumer<Book> testNotifierConsumer = new TestNotifierConsumer<>(expiredTime, book, lock);

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(10);
        TimerBorrowableItemNotifier<Book> timerBorrowableItemNotifier = new TimerBorrowableItemNotifier<>(
                scheduler,
                storage,
                testNotifierConsumer);

        timerBorrowableItemNotifier.add(book);
        Duration duration = java.time.Duration.between(Instant.now(), expiredTime);

        try {
            Thread.sleep(duration.toMillis() + 1000);

        } catch (Exception e) {
            System.out.println(e);
        }
        assertTrue(lock.tryAcquire());
    }

    private Storage<Book> createStorage() throws IOException {
        Path path = Files.createTempDirectory(UUID.randomUUID().toString());
        return new FilePersistenterStorage<>(new LinkedList<Book>(), path);
    }
}