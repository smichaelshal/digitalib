package org.plasma.digitalib.adders;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.plasma.digitalib.models.Book;
import org.plasma.digitalib.models.BookIdentifier;
import org.plasma.digitalib.storage.Storage;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class StorageBookAdderTest {
    private Predicate<Book> bookIdentifierFilter;
    private Book book;
    private StorageBookAdder storageBookAdder;
    private Storage<Book> storage;
    private List<Book> books;

    @BeforeEach
    public void setup() {
        this.book = new Book(
                "genre",
                "summary",
                new BookIdentifier("name", "author"));
        this.books = List.of(this.book);
        this.storage = mock(Storage.class);
        this.storageBookAdder = new StorageBookAdder(this.storage);
        when(this.storage.create(any(Book.class))).thenReturn(true);
    }

    @Test
    public void add_withNewBook_should_returnTrue() {
        // Arrange
        when(this.storage.readAll(any(Predicate.class))).thenReturn(this.books);

        // Act
        boolean adderResult = this.storageBookAdder.add(this.book);

        // Assert
        assertTrue(adderResult);
    }

    @Test
    public void add_withTwoBooksWithDifferentAuthor_should_returnTrue() {
        // Arrange
        when(this.storage.readAll(any(Predicate.class))).thenReturn(this.books);
        Book secondBook = new Book(
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
    public void add_withTwoBooksWithDifferentSummery_should_returnFalse() {
        // Arrange
        when(this.storage.readAll(any(Predicate.class))).thenReturn(this.books);

        // Arrange
        Book secondBook = new Book(
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
    public void add_withExistBookWithEmptySummary_should_returnTrue() {
        // Arrange
        when(this.storage.readAll(any(Predicate.class))).thenReturn(this.books);

        // Arrange
        Book secondBook = new Book(
                this.book.getGenre(),
                "",
                new BookIdentifier(
                        this.book.getBookIdentifier().getName(),
                        this.book.getBookIdentifier().getAuthor()));

        // Act
        this.storageBookAdder.add(this.book);
        boolean adderResult = this.storageBookAdder.add(secondBook);

        // Assert
        assertTrue(adderResult);
    }

    @Test
    public void add_withExistBookWithEmptyGenre_should_returnTrue() {
        // Arrange
        when(this.storage.readAll(any(Predicate.class))).thenReturn(this.books);
        Book secondBook = new Book(
                "",
                this.book.getSummary(),
                new BookIdentifier(
                        this.book.getBookIdentifier().getName(),
                        this.book.getBookIdentifier().getAuthor()));

        // Act
        this.storageBookAdder.add(this.book);
        boolean adderResult = this.storageBookAdder.add(secondBook);

        // Assert
        assertTrue(adderResult);
    }

    @Test
    public void add_withExistBookWithEmptySummaryAndGenre_should_returnTrue() {
        // Arrange
        when(this.storage.readAll(any(Predicate.class))).thenReturn(this.books);
        Book secondBook = new Book(
                "",
                "",
                new BookIdentifier(
                        this.book.getBookIdentifier().getName(),
                        this.book.getBookIdentifier().getAuthor()));

        // Act
        this.storageBookAdder.add(this.book);
        boolean adderResult = this.storageBookAdder.add(secondBook);

        // Assert
        assertTrue(adderResult);
    }

    @Test
    public void add_withExistBookWithEmptySummaryAndDifferentGenre_should_returnTrue() {
        // Arrange
        when(this.storage.readAll(any(Predicate.class))).thenReturn(this.books);
        Book secondBook = new Book(
                this.book.getGenre() + "_",
                "",
                new BookIdentifier(
                        this.book.getBookIdentifier().getName(),
                        this.book.getBookIdentifier().getAuthor()));

        // Act
        this.storageBookAdder.add(this.book);
        boolean adderResult = this.storageBookAdder.add(secondBook);

        // Assert
        assertFalse(adderResult);
    }

    @Test
    public void add_withExistBookWithDifferentSummaryAndEmptyGenre_should_returnTrue() {
        // Arrange
        when(this.storage.readAll(any(Predicate.class))).thenReturn(this.books);
        Book secondBook = new Book(
                "",
                this.book.getSummary() + "_",
                new BookIdentifier(
                        this.book.getBookIdentifier().getName(),
                        this.book.getBookIdentifier().getAuthor()));

        // Act
        this.storageBookAdder.add(this.book);
        boolean adderResult = this.storageBookAdder.add(secondBook);

        // Assert
        assertFalse(adderResult);
    }

    @Test
    public void add_withNewBookWithEmptySummaryAndGenre_shouldReturnFalse() {
        // Arrange
        when(this.storage.readAll(any(Predicate.class))).thenReturn(
                new LinkedList());
        Book book = new Book(
                "",
                "",
                new BookIdentifier(
                        this.book.getBookIdentifier().getName() + "_",
                        this.book.getBookIdentifier().getAuthor() + "_"));

        // Act
        boolean adderResult = this.storageBookAdder.add(book);

        // Assert
        assertFalse(adderResult);
    }
}