package org.plasma.digitalib.tasks;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.plasma.digitalib.adders.StorageBookAdder;
import org.plasma.digitalib.inputs.ConsoleInput;
import org.plasma.digitalib.models.Book;
import org.plasma.digitalib.models.BorrowableItem;
import org.plasma.digitalib.storage.FilePersistenterStorage;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.UUID;

class AddNewBookTaskTest {

    @Test
    public void run() throws IOException {
        // Arrange
        ByteArrayInputStream inputStream =
                new ByteArrayInputStream("a\nb\nc\nd\n".getBytes());

        Scanner scanner = new Scanner(inputStream);

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

        FilePersistenterStorage<Book> storage = new FilePersistenterStorage(
                new LinkedList(),
                Files.createTempDirectory(UUID.randomUUID().toString()),
                objectMapper);
        StorageBookAdder storageBookAdder = new StorageBookAdder(storage);
        ConsoleInput consoleUtils = new ConsoleInput(scanner);
        ConsoleCreatorBookIdentifier creatorBookIdentifier =
                new ConsoleCreatorBookIdentifier(consoleUtils);
        Task task = new AddBookCopyTask("add new book", storageBookAdder,
                creatorBookIdentifier, consoleUtils);

        // Act
        task.run();

        // Assert
    }
}