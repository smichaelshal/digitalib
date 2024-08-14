package org.plasma.digitalib.storage;

import org.junit.jupiter.api.Test;
import org.plasma.digitalib.dtos.Book;
import org.plasma.digitalib.dtos.BookIdentifier;
import org.plasma.digitalib.dtos.Borrowing;
import org.plasma.digitalib.dtos.User;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.apache.commons.lang3.SerializationUtils;


import static org.junit.jupiter.api.Assertions.*;

class FilePersistenterStorageTest {

    private Storage<Book> createStorage() throws IOException {
        Path path = Files.createTempDirectory(UUID.randomUUID().toString());
        return new FilePersistenterStorage<>(new LinkedList<Book>(), path);
    }

    private Book createBook() {
        Book book = new Book("genre", "summary", new BookIdentifier("name", "author"));
        Instant expiredTime = Instant.now().plus(3, ChronoUnit.SECONDS);
        book.getBorrowings().add(new Borrowing(new User("1234"), Instant.now(), expiredTime));
        return book;
    }

    @Test
    public void addBookTest() throws IOException {
        Storage<Book> storage = this.createStorage();
        Book book = this.createBook();
        storage.create(book);

        AnyBookFilter anyBookFilter = new AnyBookFilter();
//        BookByIdFilter bookByIdFilter = new BookByIdFilter(book.getId());

        List<Book> books = storage.readAll(anyBookFilter);
        assertEquals(1, books.size());

        Book bookFromStorage = books.get(0);
        assertEquals(book.getBookIdentifier(), bookFromStorage.getBookIdentifier());
    }

    @Test
    public void readBookTest() {

    }

    @Test
    public void updateBookTest() throws IOException {
        Storage<Book> storage = this.createStorage();
        Book book = this.createBook();
        storage.create(book);

        Book bookCopy = SerializationUtils.clone(book);

        Instant expiredTime = Instant.now().plus(1000, ChronoUnit.SECONDS);
        Borrowing borrowing = new Borrowing(new User("5678"), Instant.now(), expiredTime);
        bookCopy.getBorrowings().add(borrowing);
        storage.update(book, bookCopy);

        BookByIdFilter bookByIdFilter = new BookByIdFilter(book.getId());

        List<Book> books = storage.readAll(bookByIdFilter);
        // fix check if size > 0

        Book bookResult = books.get(0);
        List<Borrowing> borrowingsResult = bookResult.getBorrowings();
        // fix equal between borrowings
//        assertEquals(borrowings.get(borrowings.size() - 1), bookFromStorage.getBookIdentifier());
    }
}