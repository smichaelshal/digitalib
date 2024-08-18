package org.plasma.digitalib.adders;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.plasma.digitalib.models.Book;
import org.plasma.digitalib.models.BookIdentifier;
import org.plasma.digitalib.storage.Storage;

import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class StorageBookAdderTest {
    private Function<Book, Boolean> bookIdentifierFilter;
    private Book book;
    private StorageBookAdder storageBookAdder;
    private Storage<Book> storage;

    @BeforeEach
    public void setup() {
        this.book = new Book(
                "genre",
                "summary",
                new BookIdentifier("name", "author"));
        List<Book> books = List.of(this.book);
        this.storage = mock(Storage.class);
        when(this.storage.readAll(any(Function.class))).thenReturn(books);
        this.storageBookAdder = new StorageBookAdder(this.storage);
        when(this.storage.create(any(Book.class))).thenReturn(true);
    }

    @Test
    public void add_withNewBook_should_returnTrue() {
        // Act
        boolean adderResult = this.storageBookAdder.add(this.book);

        // Assert
        assertTrue(adderResult);
    }

    @Test
    public void add_withTwoBooksWithDifferentAuthor_should_returnTrue() {
        // Arrange
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
}