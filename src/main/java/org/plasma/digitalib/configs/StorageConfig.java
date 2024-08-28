package org.plasma.digitalib.configs;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.plasma.digitalib.models.Book;
import org.plasma.digitalib.storage.FilePersistenterStorage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedList;
import java.util.List;

@Configuration
@ComponentScan(basePackageClasses = FilePersistenterStorage.class)
public class StorageConfig {

    @Bean
    public static List<Book> listStorage() {
        return new LinkedList<>();
    }

    @Bean
    public static String pathDirectoryRecover() {
        return "digitalib/items";
    }

    @Bean
    public static ObjectMapper objectMapperStorage() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.registerModule(new Jdk8Module());

        return objectMapper;
    }

    @Bean
    public static FilePersistenterStorage<Book> bookFilePersistenterStorage(
            final List<Book> listStorage, final String pathDirectoryRecover,
            final ObjectMapper objectMapperStorage) {
        return new FilePersistenterStorage<>(listStorage, pathDirectoryRecover,
                objectMapperStorage, new TypeReference<>() { });
    }
}

