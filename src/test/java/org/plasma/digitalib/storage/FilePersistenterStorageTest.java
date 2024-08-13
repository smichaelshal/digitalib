package org.plasma.digitalib.storage;

import org.junit.jupiter.api.Test;
import org.plasma.digitalib.dtos.Book;
import org.plasma.digitalib.dtos.BookIdentifier;
import org.plasma.digitalib.dtos.Borrowing;
import org.plasma.digitalib.dtos.User;

import java.nio.file.Path;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FilePersistenterStorageTest {

    private Storage<Book> createStorage() {
        // create emptry tmp directory <<<
        Path path = Path.of("C:\\Users\\Ori\\IdeaProjects\\digitalib\\src\\db");
        return new FilePersistenterStorage<>(new LinkedList<Book>(), path);
    }

    private Book createBook() {
        Book book = new Book("genre", "summary", new BookIdentifier("name", "author"));
        Instant expiredTime = Instant.now().plus(3, ChronoUnit.SECONDS);
        book.getBorrowings().add(new Borrowing(new User("1234"), Instant.now(), expiredTime));
        return book;
    }

    @Test
    public void addBookTest() {
        Storage<Book> storage = this.createStorage();
        Book book = this.createBook();
        storage.create(book);

//        AnyBookFilter anyBookFilter = new AnyBookFilter();
        BookByIdFilter bookByIdFilter = new BookByIdFilter(book.getId());

        List<Book> books = storage.readAll(bookByIdFilter);
        assertEquals(1, books.size());

        Book bookFromStorage = books.get(0);
        assertEquals(book.getBookIdentifier(), bookFromStorage.getBookIdentifier());
    }

    @Test
    public void readBookTest() {

    }

    @Test
    public void updateBookTest() {
        Storage<Book> storage = this.createStorage();
        Book book = this.createBook();
        storage.create(book);

        Instant expiredTime = Instant.now().plus(1000, ChronoUnit.SECONDS);
        book.getBorrowings().add(new Borrowing(new User("5678"), Instant.now(), expiredTime));

        // fix deep copy to test object
        storage.update(book, book);

        BookByIdFilter bookByIdFilter = new BookByIdFilter(book.getId());

        List<Book> books = storage.readAll(bookByIdFilter);
        // fix check if size > 0

        Book bookFromStorage = books.get(0);
        List<Borrowing> borrowings = book.getBorrowings();
        // fix equal between borrowings
//        assertEquals(borrowings.get(borrowings.size() - 1), bookFromStorage.getBookIdentifier());
    }
}