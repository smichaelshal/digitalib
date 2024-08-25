package org.plasma.digitalib.searchers;

import org.plasma.digitalib.models.Book;
import org.plasma.digitalib.storage.Storage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = BookSearcher.class)
public class SearcherConfig {
    @Bean
    public BookSearcher bookSearcher(final Storage<Book> storage) {
        return new BookSearcher(storage);
    }
}
