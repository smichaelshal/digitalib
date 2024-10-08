package org.plasma.digitalib.adders;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.plasma.digitalib.models.Book;
import org.plasma.digitalib.models.BookIdentifier;
import org.plasma.digitalib.storage.Storage;

import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class StorageBookAdderTest {
    private Book book;
    private StorageBookAdder storageBookAdder;
    private List<Book> books;

    @Mock
    private Storage<Book> storage;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.book = new Book(
                new LinkedList<>(),
                UUID.randomUUID(),
                Instant.now(),
                false,
                "genre",
                "summary",
                new BookIdentifier("name", "author"));
        this.books = List.of(this.book);
        this.storageBookAdder = new StorageBookAdder(this.storage);
        when(this.storage.create(any(Book.class))).thenReturn(true);
    }

    @Test
    public void add_withNewBook_shouldReturnTrue() {
        // Arrange
        when(this.storage.readAll(any())).thenReturn(this.books);

        // Act
        boolean adderResult = this.storageBookAdder.add(this.book);

        // Assert
        assertTrue(adderResult);
    }

    @Test
    public void add_withNewBookWhenStorageCreateFailed_shouldReturnFalse() {
        // Arrange
        when(this.storage.create(any(Book.class))).thenReturn(false);
        when(this.storage.readAll(any())).thenReturn(new LinkedList<>());

        // Act
        boolean adderResult = this.storageBookAdder.add(this.book);

        // Assert
        assertFalse(adderResult);
    }

    @Test
    public void add_withNewBook_shouldCallStorageCreate() {
        // Arrange
        when(this.storage.readAll(any())).thenReturn(this.books);

        // Act
        this.storageBookAdder.add(this.book);

        // Assert
        verify(this.storage, times(1)).create(this.book);
    }

    @Test
    public void add_withTwoBooksWithDifferentAuthor_shouldReturnTrue() {
        // Arrange
        when(this.storage.readAll(any())).thenReturn(this.books);
        Book secondBook = new Book(
                new LinkedList<>(),
                UUID.randomUUID(),
                Instant.now(),
                false,
                this.book.getGenre(),
                this.book.getSummary(),
                new BookIdentifier(
                        this.book.getBookIdentifier().getName(),
                        this.book.getBookIdentifier().getAuthor() + "_"));
        this.storageBookAdder.add(this.book);

        // Act
        boolean adderResult = this.storageBookAdder.add(secondBook);

        // Assert
        assertTrue(adderResult);
    }

    @Test
    public void add_withTwoBooksWithDifferentSummery_shouldReturnFalse() {
        // Arrange
        when(this.storage.readAll(any())).thenReturn(this.books);
        Book secondBook = new Book(
                new LinkedList<>(),
                UUID.randomUUID(),
                Instant.now(),
                false,
                this.book.getGenre(),
                this.book.getSummary() + "_",
                new BookIdentifier(
                        this.book.getBookIdentifier().getName(),
                        this.book.getBookIdentifier().getAuthor()));
        this.storageBookAdder.add(this.book);

        // Act
        boolean adderResult = this.storageBookAdder.add(secondBook);

        // Assert
        assertFalse(adderResult);
    }

    @Test
    public void add_withTwoBooksWithDifferentSummery_shouldNotCallStorageCreate() {
        // Arrange
        when(this.storage.readAll(any())).thenReturn(this.books);
        Book secondBook = new Book(
                new LinkedList<>(),
                UUID.randomUUID(),
                Instant.now(),
                false,
                this.book.getGenre(),
                this.book.getSummary() + "_",
                new BookIdentifier(
                        this.book.getBookIdentifier().getName(),
                        this.book.getBookIdentifier().getAuthor()));
        this.storageBookAdder.add(this.book);

        // Act
        this.storageBookAdder.add(secondBook);

        // Assert
        verify(this.storage, never()).create(secondBook);
    }

    @Test
    public void add_withExistBookWithEmptySummary_shouldReturnTrue() {
        // Arrange
        when(this.storage.readAll(any())).thenReturn(this.books);
        Book secondBook = new Book(
                new LinkedList<>(),
                UUID.randomUUID(),
                Instant.now(),
                false,
                this.book.getGenre(),
                "",
                new BookIdentifier(
                        this.book.getBookIdentifier().getName(),
                        this.book.getBookIdentifier().getAuthor()));
        this.storageBookAdder.add(this.book);

        // Act
        boolean adderResult = this.storageBookAdder.add(secondBook);

        // Assert
        assertTrue(adderResult);
    }

    @Test
    public void add_withExistBookWithEmptyGenre_shouldReturnTrue() {
        // Arrange
        when(this.storage.readAll(any())).thenReturn(this.books);
        Book secondBook = new Book(
                new LinkedList<>(),
                UUID.randomUUID(),
                Instant.now(),
                false,
                "",
                this.book.getSummary(),
                new BookIdentifier(
                        this.book.getBookIdentifier().getName(),
                        this.book.getBookIdentifier().getAuthor()));
        this.storageBookAdder.add(this.book);

        // Act
        boolean adderResult = this.storageBookAdder.add(secondBook);

        // Assert
        assertTrue(adderResult);
    }

    @Test
    public void add_withExistBookWithEmptySummaryAndGenre_shouldReturnTrue() {
        // Arrange
        when(this.storage.readAll(any())).thenReturn(this.books);
        Book secondBook = new Book(
                new LinkedList<>(),
                UUID.randomUUID(),
                Instant.now(),
                false,
                "",
                "",
                new BookIdentifier(
                        this.book.getBookIdentifier().getName(),
                        this.book.getBookIdentifier().getAuthor()));
        this.storageBookAdder.add(this.book);

        // Act
        boolean adderResult = this.storageBookAdder.add(secondBook);

        // Assert
        assertTrue(adderResult);
    }

    @Test
    public void add_withExistBookWithEmptySummaryAndDifferentGenre_shouldReturnFalse() {
        // Arrange
        when(this.storage.readAll(any())).thenReturn(this.books);
        Book secondBook = new Book(
                new LinkedList<>(),
                UUID.randomUUID(),
                Instant.now(),
                false,
                this.book.getGenre() + "_",
                "",
                new BookIdentifier(
                        this.book.getBookIdentifier().getName(),
                        this.book.getBookIdentifier().getAuthor()));
        this.storageBookAdder.add(this.book);

        // Act
        boolean adderResult = this.storageBookAdder.add(secondBook);

        // Assert
        assertFalse(adderResult);
    }

    @Test
    public void add_withExistBookWithDifferentSummaryAndEmptyGenre_shouldReturnFalse() {
        // Arrange
        when(this.storage.readAll(any())).thenReturn(this.books);
        Book secondBook = new Book(
                new LinkedList<>(),
                UUID.randomUUID(),
                Instant.now(),
                false,
                "",
                this.book.getSummary() + "_",
                new BookIdentifier(
                        this.book.getBookIdentifier().getName(),
                        this.book.getBookIdentifier().getAuthor()));
        this.storageBookAdder.add(this.book);

        // Act
        boolean adderResult = this.storageBookAdder.add(secondBook);

        // Assert
        assertFalse(adderResult);
    }

    @Test
    public void add_withNewBookWithEmptySummaryAndGenre_shouldReturnFalse() {
        // Arrange
        when(this.storage.readAll(any())).thenReturn(
                new LinkedList<>());
        Book book =
                new Book(
                        new LinkedList<>(),
                        UUID.randomUUID(),
                        Instant.now(),
                        false,
                        "",
                        "",
                        new BookIdentifier(
                                this.book.getBookIdentifier().getName() + "_",
                                this.book.getBookIdentifier().getAuthor() +
                                        "_"));

        // Act
        boolean adderResult = this.storageBookAdder.add(book);

        // Assert
        assertFalse(adderResult);
    }

    @Test
    public void add_withNewBookWithEmptyNameAndAuthor_shouldReturnFalse() {
        // Arrange
        when(this.storage.readAll(any())).thenReturn(
                new LinkedList<>());
        Book book = new Book(
                new LinkedList<>(),
                UUID.randomUUID(),
                Instant.now(),
                false,
                this.book.getGenre(),
                this.book.getSummary(),
                new BookIdentifier(
                        "",
                        ""));

        // Act
        boolean adderResult = this.storageBookAdder.add(book);

        // Assert
        assertFalse(adderResult);
    }

    @Test
    public void add_withNewBookWithEmptyAuthor_shouldReturnFalse() {
        // Arrange
        when(this.storage.readAll(any())).thenReturn(
                new LinkedList<>());
        Book book = new Book(
                new LinkedList<>(),
                UUID.randomUUID(),
                Instant.now(),
                false,
                this.book.getGenre(),
                this.book.getSummary(),
                new BookIdentifier(
                        this.book.getBookIdentifier().getName(),
                        ""));

        // Act
        boolean adderResult = this.storageBookAdder.add(book);

        // Assert
        assertFalse(adderResult);
    }

    @Test
    public void add_withNewBookWithEmptyName_shouldReturnFalse() {
        // Arrange
        when(this.storage.readAll(any())).thenReturn(
                new LinkedList<>());
        Book book = new Book(
                new LinkedList<>(),
                UUID.randomUUID(),
                Instant.now(),
                false,
                this.book.getGenre(),
                this.book.getSummary(),
                new BookIdentifier(
                        "",
                        this.book.getBookIdentifier().getName()));

        // Act
        boolean adderResult = this.storageBookAdder.add(book);

        // Assert
        assertFalse(adderResult);
    }
}

