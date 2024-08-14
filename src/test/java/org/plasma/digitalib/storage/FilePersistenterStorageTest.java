package org.plasma.digitalib.storage;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.commons.lang3.SerializationUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.plasma.digitalib.dtos.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FilePersistenterStorageTest {

    private List<Book> listStorage;
    private Storage<Book> storage;
    private Book book;

    @BeforeEach
    public void setup() throws IOException {
        this.listStorage = new LinkedList<Book>();
        Path path = Files.createTempDirectory(UUID.randomUUID().toString());
        this.storage = new FilePersistenterStorage<>(this.listStorage, path);
        this.book = new Book(
                "genre",
                "summary",
                new BookIdentifier("name", "author"));
    }

    @Test
    public void add_with_book_should_add_to_list() {
        // arrange
        int sizeListBefore = this.listStorage.size();

        // act
        this.storage.create(this.book);
        Book bookResult = this.listStorage.get(sizeListBefore);
        int sizeListAfter = this.listStorage.size();
        boolean isEquals = this.compare(book, bookResult);

        // assert
        assertEquals(sizeListBefore + 1, sizeListAfter);
        assertTrue(isEquals);
    }

    @Test
    public void read_with_id_filter_should_return_book() {
        // arrange
        BookByIdFilter bookByIdFilter = new BookByIdFilter(this.book.getId());
        this.storage.create(this.book);

        // act
        List<Book> bookResults = this.storage.readAll(bookByIdFilter);
        int sizeResult = bookResults.size();
        boolean isEquals = this.compare(this.book, bookResults.get(0));

        // assert
        assertEquals(1, sizeResult);
        assertTrue(isEquals);
    }

    @Test
    public void update_with_new_borrowing_return_updated_book() {
        // arrange
        this.storage.create(this.book);
        Book bookCopy = SerializationUtils.clone(this.book);
        Borrowing borrowing = new Borrowing(
                new User("5678"),
                Instant.now(),
                Instant.now().plus(1000, ChronoUnit.SECONDS));
        bookCopy.getBorrowings().add(borrowing);

        // act
        this.storage.update(this.book, bookCopy);
        Book bookResult = this.listStorage.stream()
                .filter(book -> book.getId().equals(this.book.getId()))
                .findFirst().get();
        boolean isEquals = this.compare(bookCopy, bookResult);

        // assert
        assertTrue(isEquals);
    }

    private <T> boolean compare(T fistItem, T secondItem) {
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

        try {
            return Objects.equals(objectMapper.writeValueAsString(fistItem),
                    objectMapper.writeValueAsString(secondItem));
        } catch (JsonProcessingException e) {
            // log
            return false;
        }
    }
}