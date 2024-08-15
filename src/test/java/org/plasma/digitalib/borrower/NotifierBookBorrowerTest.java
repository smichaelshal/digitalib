package org.plasma.digitalib.borrower;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.plasma.digitalib.models.*;
import org.plasma.digitalib.storage.Storage;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class NotifierBookBorrowerTest {
    private NotifierBookBorrower borrower;
    private UpdaterBookBorrower updater;

    private Book book;
    private OrderRequest<BookIdentifier> orderRequest;
    private Storage<Book> storage;
    private User user;

    @BeforeEach
    void setup() {
        this.book = new Book(
                "genre",
                "summary",
                new BookIdentifier("name", "author"));

        this.user = new User("1234");


        this.orderRequest = new OrderRequest<>(user, this.book.getBookIdentifier());

        this.storage = mock(Storage.class);
        BorrowableItemNotifier<Book> notifier = mock(BorrowableItemNotifier.class);

        this.updater = new UpdaterBookBorrower(
                this.storage,
                Duration.of(1, ChronoUnit.DAYS));

        this.borrower = new NotifierBookBorrower(this.updater, notifier, this.storage);



        when(notifier.add(any(Book.class))).thenReturn(true);
        when(notifier.delete(any(Book.class))).thenReturn(true);
    }

    @Test
    void borrow_withBookIdentifierNotExist_shouldReturnSuccess() {
        // Arrange
        List<Book> books = List.of(this.book);
        when(this.storage.readAll(any(Function.class))).thenReturn(books);
        when(this.storage.update(any(UUID.class), any(Book.class))).thenReturn(true);

        // Act
        BorrowingResult borrowingResult = this.borrower.borrowItem(this.orderRequest);

        // Assert
        assertEquals(BorrowingResult.SUCCESS, borrowingResult);
    }

    @Test
    void borrow_withBookIdentifierNotExist_shouldReturnNotExist() {
        // Arrange
        when(this.storage.readAll(any(Function.class))).thenReturn(new LinkedList());

        // Act
        BorrowingResult borrowingResult = this.borrower.borrowItem(this.orderRequest);

        // Assert
        assertEquals(BorrowingResult.NOT_EXIST, borrowingResult);
    }

    @Test
    void borrow_withBookIdentifierBorrowed_shouldReturnOutOfStock() {
        // Arrange
        when(this.storage.readAll(any(Function.class))).thenReturn(List.of(this.book));
        when(this.storage.update(any(UUID.class), any(Book.class))).thenReturn(true);

        // Act
        this.borrower.borrowItem(this.orderRequest);
        BorrowingResult borrowingResult = this.borrower.borrowItem(this.orderRequest);

        // Assert
        assertEquals(BorrowingResult.OUT_OF_STOCK, borrowingResult);
    }

    @Test
    void return_withBookIdentifier_shouldReturnTrue() {
        // Arrange
        this.book.getBorrowings().add(new Borrowing(
                this.user,
                Instant.now(),
                Optional.of(Instant.now()),
                Instant.now()));

        this.book.setIsBorrowed(true);

        when(this.storage.readAll(any(Function.class))).thenReturn(List.of(this.book));
        when(this.storage.update(any(UUID.class), any(Book.class))).thenReturn(true);

        // Act
        boolean borrowingResult = this.borrower.returnItem(this.orderRequest);

        // Assert
        assertTrue(borrowingResult);
    }

    @Test
    void return_withBookIdentifierDifferentUser_shouldReturnFalse() {
        // Arrange
        User otherUser = new User(this.user + "_");
        this.book.getBorrowings().add(new Borrowing(
                otherUser,
                Instant.now(),
                Optional.of(Instant.now()),
                Instant.now()));

        this.book.setIsBorrowed(true);

        when(this.storage.readAll(any(Function.class))).thenReturn(List.of(this.book));
        when(this.storage.update(any(UUID.class), any(Book.class))).thenReturn(true);

        // Act
        boolean borrowingResult = this.borrower.returnItem(this.orderRequest);

        // Assert
        assertFalse(borrowingResult);
    }

}