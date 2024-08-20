package org.plasma.digitalib.borrower;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.plasma.digitalib.models.Book;
import org.plasma.digitalib.models.BookIdentifier;
import org.plasma.digitalib.models.Borrowing;
import org.plasma.digitalib.models.User;
import org.plasma.digitalib.storage.Storage;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class TimerBorrowableItemNotifierTest {
    private Book book;
    private TimerBorrowableItemNotifier<Book> notifier;
    private Consumer<Book> consumer;
    private Storage<Book> storage;
    private ScheduledExecutorService scheduler;

    @BeforeEach
    public void setup() throws IOException {
        this.storage = mock(Storage.class);
        this.book = new Book(
                "genre",
                "summary",
                new BookIdentifier("name", "author"));
        Instant expiredTime = Instant.now().plus(3, ChronoUnit.SECONDS);
        User user = new User("1234");
        this.book.getBorrowings().add(
                new Borrowing(user, Instant.now(), Optional.empty(),
                        expiredTime));
        this.scheduler = Executors.newScheduledThreadPool(10);
        this.consumer = mock(Consumer.class);
    }

    private void createNotifier() {
        this.notifier = new TimerBorrowableItemNotifier<>(
                this.scheduler,
                this.storage,
                this.consumer);
    }

    @Test
    public void constructor_withExistBookOnStorage_shouldCallWithBookOnTime() {
        // Arrange
        List<Book> books = List.of(this.book);
        when(this.storage.readAll(any(Predicate.class))).thenReturn(books);
        long expiredTime = this.book.getBorrowings().get(0).getExpiredTime()
                .toEpochMilli();
        long deltaTime = Duration.between(Instant.now(),
                this.book.getBorrowings().get(0).getExpiredTime()).toMillis();

        // Act
        new TimerBorrowableItemNotifier<>(
                this.scheduler,
                this.storage,
                this.consumer);

        // Assert
        verify(this.consumer, timeout(deltaTime + 1000).times(1)).accept(
                this.book);

        long endTime = Instant.now().toEpochMilli();
        boolean isEndTimeBigThanExpiredTime = endTime >= expiredTime;
        assertTrue(isEndTimeBigThanExpiredTime);
    }

    @Test
    public void add_withBook_shouldCallWithBookOnTime() {
        // Arrange
        this.createNotifier();
        List<Book> books = List.of(this.book);
        when(this.storage.readAll(any(Predicate.class))).thenReturn(books);
        long expiredTime = this.book.getBorrowings().get(0).getExpiredTime()
                .toEpochMilli();
        long deltaTime = Duration.between(Instant.now(),
                this.book.getBorrowings().get(0).getExpiredTime()).toMillis();

        // Act
        boolean notifierResult = this.notifier.add(this.book);

        // Assert
        verify(this.consumer, timeout(deltaTime + 1000).times(1)).accept(
                this.book);

        long endTime = Instant.now().toEpochMilli();
        boolean isEndTimeBigThanExpiredTime = endTime >= expiredTime;
        assertTrue(isEndTimeBigThanExpiredTime);
        assertTrue(notifierResult);
    }

    @Test
    public void add_withExistBookOnStorage_shouldReturnFalse() {
        // Arrange
        this.createNotifier();
        List<Book> books = List.of(this.book);
        when(this.storage.readAll(any(Predicate.class))).thenReturn(books);
        TimerBorrowableItemNotifier<Book> timerBorrowableItemNotifier =
                new TimerBorrowableItemNotifier<>(
                        Executors.newScheduledThreadPool(10),
                        this.storage,
                        this.consumer);

        // Act
        boolean addResult = timerBorrowableItemNotifier.add(this.book);

        // Assert
        assertFalse(addResult);
    }

    @Test
    public void delete_withBook_shouldNotCall() {
        // Arrange
        this.createNotifier();
        List<Book> books = List.of(this.book);
        when(this.storage.readAll(any(Predicate.class))).thenReturn(books);
        long deltaTime = Duration.between(Instant.now(),
                this.book.getBorrowings().get(0).getExpiredTime()).toMillis();

        // Act
        boolean addResult = this.notifier.add(this.book);
        boolean deleteResult = this.notifier.delete(this.book);

        // Assert
        verify(this.consumer, after(deltaTime + 1000).never()).accept(
                this.book);
        assertTrue(addResult);
        assertTrue(deleteResult);
    }

    @Test
    public void delete_withNotExistBook_shouldReturnFalse() {
        // Arrange
        this.createNotifier();
        List<Book> books = List.of(this.book);
        when(this.storage.readAll(any(Predicate.class))).thenReturn(books);

        // Act
        boolean deleteResult = this.notifier.delete(this.book);

        // Assert
        assertFalse(deleteResult);
    }
}