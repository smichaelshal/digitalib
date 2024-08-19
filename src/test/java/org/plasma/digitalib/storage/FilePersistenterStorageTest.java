package org.plasma.digitalib.storage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.commons.lang3.SerializationUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.plasma.digitalib.models.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FilePersistenterStorageTest {

    private List<Book> listStorage;
    private List<Book> books;
    private FilePersistenterStorage<Book> storage;
    private Book book;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() throws IOException {
        this.books = new LinkedList<>();
        this.listStorage = new LinkedList<Book>();
        Path path = Files.createTempDirectory(UUID.randomUUID().toString());

        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.registerModule(new Jdk8Module());

        PolymorphicTypeValidator polymorphicTypeValidator =
                BasicPolymorphicTypeValidator.builder()
                        .allowIfSubType("org.plasma.digitalib")
                        .allowIfSubType("java.util.LinkedList")
                        .build();
        this.objectMapper.activateDefaultTyping(
                polymorphicTypeValidator,
                ObjectMapper.DefaultTyping.NON_FINAL);
        this.objectMapper.registerSubtypes(BorrowableItem.class);

        this.storage = new FilePersistenterStorage<>(this.listStorage, path, this.objectMapper);
        this.book = new Book(
                "genre",
                "summary",
                new BookIdentifier("name", "author"));
    }

    @Test
    public void add_withBook_shouldAddToList() {
        // Arrange
        this.books.add(this.book);

        // Act
        boolean addResult = this.storage.create(this.book);

        // Assert
        assertTrue(addResult);
        assertEquals(this.books, this.listStorage);
    }

    @Test
    public void read_withIdFilter_shouldReturnBook() {
        // Arrange
        Predicate<Book> bookByIdFilter = mock(Predicate.class);
        when(bookByIdFilter.test(this.book)).thenReturn(true);

        this.storage.create(this.book);

        // Act
        List<Book> bookResults = this.storage.readAll(bookByIdFilter);

        // Assert
        assertEquals(List.of(this.book), bookResults);
    }

    @Test
    public void update_withNewBorrowing_shouldReturnUpdatedBook() {
        // Arrange

        this.storage.create(this.book);
        Book bookCopy = SerializationUtils.clone(this.book);
        Borrowing borrowing = new Borrowing(
                new User("5678"),
                Instant.now(),
                Instant.now().plus(1000, ChronoUnit.SECONDS));
        bookCopy.getBorrowings().add(borrowing);
        this.books.add(bookCopy);

        // Act
        boolean updateResult = this.storage.update(this.book.getId(), bookCopy);

        // Assert
        assertTrue(updateResult);
        assertEquals(this.books, this.listStorage);
    }
}