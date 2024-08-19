package org.plasma.digitalib;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.plasma.digitalib.adders.StorageBookAdder;
import org.plasma.digitalib.models.Book;
import org.plasma.digitalib.models.BorrowableItem;
import org.plasma.digitalib.storage.FilePersistenterStorage;
import org.plasma.digitalib.tasks.AddNewBookTask;
import org.plasma.digitalib.tasks.ConsoleCreatorBookIdentifier;
import org.plasma.digitalib.tasks.ConsoleUtils;
import org.plasma.digitalib.tasks.Task;

import java.io.IOException;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.UUID;

public final class Application {
    public static void main(final String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
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
        ConsoleUtils consoleUtils = new ConsoleUtils(scanner);
        ConsoleCreatorBookIdentifier creatorBookIdentifier =
                new ConsoleCreatorBookIdentifier(consoleUtils);
        Task task = new AddNewBookTask("add new book", storageBookAdder,
                creatorBookIdentifier, consoleUtils);
        task.run();
        task.run();
    }
}
