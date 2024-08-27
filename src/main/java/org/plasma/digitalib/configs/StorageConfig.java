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

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

@Configuration
@ComponentScan(basePackageClasses = FilePersistenterStorage.class)
public class StorageConfig {
    /**
     * @return
     */
    @Bean
    public List<Book> listStorage() {
        return new LinkedList<>();
    }

    /**
     * @return
     * @throws IOException
     */
    @Bean
    public String pathDirectoryRecover() throws IOException {
        return "digitalib/items";
    }

    /**
     * @return
     */
    @Bean
    public ObjectMapper objectMapperStorage() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.registerModule(new Jdk8Module());

        return objectMapper;
    }

    /**
     * @param listStorage
     * @param pathDirectoryRecover
     * @param objectMapperStorage
     * @return
     */
    @Bean
    public FilePersistenterStorage<Book> bookFilePersistenterStorage(
            final List<Book> listStorage,
            final String pathDirectoryRecover,
            final ObjectMapper objectMapperStorage) {
        return new FilePersistenterStorage<>(
                listStorage,
                pathDirectoryRecover,
                objectMapperStorage,
                new TypeReference<Book>() {
                });
    }
}

