package org.plasma.digitalib.borrower;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.plasma.digitalib.models.Book;
import org.plasma.digitalib.models.BookIdentifier;
import org.plasma.digitalib.models.Borrowing;
import org.plasma.digitalib.models.BorrowingResult;
import org.plasma.digitalib.models.OrderRequest;
import org.plasma.digitalib.models.User;
import org.plasma.digitalib.storage.Storage;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class NotifierBookBorrowerTest {
    private NotifierBookBorrower borrower;
    private Book book;
    private OrderRequest<BookIdentifier> orderRequest;
    private Storage<Book> storage;
    private User user;
    private Duration duration;

    @BeforeEach
    void setup() {
        this.book = new Book(
                "genre",
                "summary",
                new BookIdentifier("name", "author"));
        this.user = new User("1234");
        this.orderRequest =
                new OrderRequest<>(user, this.book.getBookIdentifier());
        this.storage = mock(Storage.class);
        this.duration = Duration.of(1, ChronoUnit.DAYS);

        BorrowableItemNotifier<Book> notifier =
                mock(BorrowableItemNotifier.class);
        this.borrower =
                new NotifierBookBorrower(notifier, this.storage,
                        this.duration);

        when(notifier.add(any(Book.class))).thenReturn(true);
        when(notifier.delete(any(Book.class))).thenReturn(true);
    }

    @Test
    void borrow_whenBookIdentifierExist_shouldReturnSuccess() {
        // Arrange
        List<Book> books = List.of(this.book);
        when(this.storage.readAll(any())).thenReturn(books);
        when(this.storage.update(any(UUID.class), any(Book.class)))
                .thenReturn(true);

        // Act
        BorrowingResult borrowingResult =
                this.borrower.borrowItem(this.orderRequest);

        // Assert
        assertEquals(BorrowingResult.SUCCESS, borrowingResult);
    }

    @Test
    void borrow_whenBookIdentifierExist_shouldCallStorageUpdateWithNewBorrowing() {
        // Arrange
        List<Book> books = List.of(this.book);
        Book copyBook = SerializationUtils.clone(this.book);
        copyBook.getBorrowings().add(new Borrowing(
                this.user,
                null,
                Optional.empty(),
                null));
        copyBook.setIsBorrowed(true);
        when(this.storage.readAll(any())).thenReturn(books);
        BookBorrowingMatcher bookBorrowingMatcher = new BookBorrowingMatcher(
                copyBook, this.duration);
        when(this.storage.update(any(UUID.class), any(Book.class)))
                .thenReturn(true);
        verify(this.storage, times(1))
                .update(this.book.getId(), argThat(bookBorrowingMatcher));

        // Act
        BorrowingResult borrowingResult =
                this.borrower.borrowItem(this.orderRequest);

        // Assert
        assertEquals(BorrowingResult.SUCCESS, borrowingResult);
    }

    @Test
    void borrow_withBookIdentifierNotExist_shouldReturnNotExist() {
        // Arrange
        when(this.storage.readAll(any()))
                .thenReturn(new LinkedList());

        // Act
        BorrowingResult borrowingResult =
                this.borrower.borrowItem(this.orderRequest);

        // Assert
        assertEquals(BorrowingResult.NOT_EXIST, borrowingResult);
    }

    @Test
    void borrow_withBookIdentifierBorrowed_shouldReturnOutOfStock() {
        // Arrange
        when(this.storage.readAll(any()))
                .thenReturn(List.of(this.book));
        when(this.storage.update(any(UUID.class), any(Book.class)))
                .thenReturn(true);

        // Act
        this.borrower.borrowItem(this.orderRequest);
        BorrowingResult borrowingResult =
                this.borrower.borrowItem(this.orderRequest);

        // Assert
        assertEquals(BorrowingResult.OUT_OF_STOCK, borrowingResult);
    }

    @Test
    void return_withAvalidBookIdentifier_shouldReturnTrue() {
        // Arrange
        this.book.getBorrowings().add(new Borrowing(
                this.user,
                Instant.now(),
                Optional.of(Instant.now()),
                Instant.now()));
        this.book.setIsBorrowed(true);

        when(this.storage.readAll(any()))
                .thenReturn(List.of(this.book));
        when(this.storage.update(any(UUID.class), any(Book.class)))
                .thenReturn(true);

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

        when(this.storage.readAll(any()))
                .thenReturn(List.of(this.book));
        when(this.storage.update(any(UUID.class), any(Book.class)))
                .thenReturn(true);

        // Act
        boolean borrowingResult = this.borrower.returnItem(this.orderRequest);

        // Assert
        assertFalse(borrowingResult);
    }
}