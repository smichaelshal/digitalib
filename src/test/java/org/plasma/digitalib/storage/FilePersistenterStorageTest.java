package org.plasma.digitalib.storage;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.plasma.digitalib.dtos.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

import org.apache.commons.lang3.SerializationUtils;


import static org.junit.jupiter.api.Assertions.*;

class FilePersistenterStorageTest {

    private List<Book> listStorage;

    private boolean compare(Book fistBook, Book secondBook) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.registerModule(new Jdk8Module());

        PolymorphicTypeValidator polymorphicTypeValidator =
                BasicPolymorphicTypeValidator.builder()
                        .allowIfSubType("org.plasma.digitalib")
                        .allowIfSubType("java.util.LinkedList")
                        .build();
        objectMapper.activateDefaultTyping(
                polymorphicTypeValidator,
                ObjectMapper.DefaultTyping.NON_FINAL);
        objectMapper.registerSubtypes(BorrowableItem.class);

        try{
            return Objects.equals(objectMapper.writeValueAsString(fistBook),
                    objectMapper.writeValueAsString(secondBook));
        } catch (JsonProcessingException e) {
            // log
            return false;
        }
    }

    private Storage<Book> createStorage() throws IOException {
        this.listStorage = new LinkedList<Book>();
        Path path = Files.createTempDirectory(UUID.randomUUID().toString());
        return new FilePersistenterStorage<>(this.listStorage, path);
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

        assertEquals(1, this.listStorage.size());
        Book bookResult = this.listStorage.get(0);
        assertTrue(this.compare(book, bookResult));
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