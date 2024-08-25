package org.plasma.digitalib.storage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.plasma.digitalib.models.Book;
import org.plasma.digitalib.models.BorrowableItem;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Configuration
@ComponentScan(basePackageClasses = FilePersistenterStorage.class)
public class StorageConfig {
    @Bean
    public List<Book> listStorage() {
        return new LinkedList<>();
    }

    @Bean
    public String pathDirectoryRecover() throws IOException {
        return "C:\\Users\\Ori\\Desktop\\db";
//        return Files.createTempDirectory(UUID.randomUUID().toString())
//                .toString();
    }

    @Bean
    public ObjectMapper objectMapperStorage() {
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
        return objectMapper;
    }

    @Bean
    public FilePersistenterStorage<Book> bookFilePersistenterStorage(
            final List<Book> listStorage,
            final String pathDirectoryRecover,
            final ObjectMapper objectMapperStorage) {
        return new FilePersistenterStorage<>(
                listStorage,
                pathDirectoryRecover,
                objectMapperStorage);
    }
}

