package org.plasma.digitalib.configs;

import org.plasma.digitalib.adders.StorageBookAdder;
import org.plasma.digitalib.models.Book;
import org.plasma.digitalib.storage.Storage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = StorageBookAdder.class)
public class AdderConfig {

    @Bean
    public static StorageBookAdder storageBookAdder(
            final Storage<Book> storage) {
        return new StorageBookAdder(storage);
    }
}
