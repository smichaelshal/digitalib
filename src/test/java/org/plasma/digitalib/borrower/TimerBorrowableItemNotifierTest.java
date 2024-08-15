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
        Storage<Book> storage = mock(Storage.class);

        this.book = new Book(
                "genre",
                "summary",
                new BookIdentifier("name", "author"));

        Instant expiredTime = Instant.now().plus(3, ChronoUnit.SECONDS);
        User user = new User("1234");

        this.book.getBorrowings().add(
                new Borrowing(user, Instant.now(), Optional.empty(), expiredTime));

        List<Book> books = List.of(this.book);

        when(storage.readAll(any(Function.class))).thenReturn(books);

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(10);

        this.consumer = mock(Consumer.class);

        this.notifier = new TimerBorrowableItemNotifier<>(
                scheduler,
                storage,
                this.consumer);
    }

    @Test
    public void add_notify_with_book_should_call_with_book_on_time() {
        // arrange
        long expiredTime = this.book.getBorrowings().get(0).getExpiredTime().toEpochMilli();
        long deltaTime = Duration.between(Instant.now(), this.book.getBorrowings().get(0).getExpiredTime()).toMillis();

        // Act
        this.notifier.add(this.book);

        // Assert
        verify(this.consumer, timeout(deltaTime + 1000).times(1)).accept(this.book);

        long endTime = Instant.now().toEpochMilli();
        boolean isEndTimeBigThanExpiredTime = endTime >= expiredTime;
        assertTrue(isEndTimeBigThanExpiredTime);
    }

    @Test
    public void delete_notify_with_book_should_not_call() {
        // arrange
        long expiredTime = this.book.getBorrowings().get(0).getExpiredTime().toEpochMilli();
        long deltaTime = Duration.between(Instant.now(), this.book.getBorrowings().get(0).getExpiredTime()).toMillis();

        // Act
        this.notifier.add(this.book);
        this.notifier.delete(this.book);

        // Assert
        verify(this.consumer, after(deltaTime + 1000).never()).accept(this.book);
    }
}