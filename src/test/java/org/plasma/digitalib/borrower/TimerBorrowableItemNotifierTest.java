package org.plasma.digitalib.borrower;

import com.sun.jdi.InternalException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
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
import java.util.function.Consumer;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class TimerBorrowableItemNotifierTest {

    private Book book;
    private TimerBorrowableItemNotifier<Book> notifier;
    private Consumer<Book> consumer;

    @BeforeEach
    public void setup() throws IOException {
//        Storage<Book> storage = mock(Storage.class);
//        Function<Book, Boolean> filter = mock(Function.class);
        Storage<Book> storage = this.createStorage();
        this.book = new Book(
                "genre",
                "summary",
                new BookIdentifier("name", "author"));

        Instant expiredTime = Instant.now().plus(3, ChronoUnit.SECONDS);
        User user = new User("1234");

        this.book.getBorrowings().add(
                new Borrowing(user, Instant.now(), Optional.empty(), expiredTime));

        List<Book> books = List.of(this.book);

        when(storage.readAll(any())).thenReturn(books);

//        TestNotifierConsumer<Book> testNotifierConsumer = new TestNotifierConsumer<>(expiredTime, book, lock);

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(10);

        this.consumer = mock(Consumer.class);

        this.notifier = new TimerBorrowableItemNotifier<>(
                scheduler,
                storage,
                this.consumer);

//        when(entityManager.find(Customer.class,1L)).thenReturn(sampleCustomer);
    }

    @Test
    public void add_notify_with_book_should_call_with_book() {

        // arrange
//        Runnable schedulerTask = () -> {
//
//        };
        long expiredTime = Duration.between(Instant.now(), this.book.getBorrowings().get(0).getExpiredTime()).toMillis();
        System.out.println(expiredTime);
        this.notifier.add(this.book);
        verify(this.consumer, timeout(expiredTime + 10000).times(1 )).accept(this.book);

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

//        Storage<Book> storage = mock(Storage.class);
//        Function<Book, Boolean> filter = mock(Function.class);
//        when(storage.readAll(any())).thenReturn(List.of(book));

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