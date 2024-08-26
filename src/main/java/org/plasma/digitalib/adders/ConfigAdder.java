package org.plasma.digitalib.adders;

import org.plasma.digitalib.models.Book;
import org.plasma.digitalib.storage.Storage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = StorageBookAdder.class)
public class ConfigAdder {
    /**
     * @param storage
     * @return
     */
    @Bean
    public StorageBookAdder storageBookAdder(final Storage<Book> storage) {
        return new StorageBookAdder(storage);
    }
}
