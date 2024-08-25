package org.plasma.digitalib;

import org.plasma.digitalib.searchers.BookSearcher;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Application {
    public static void main(final String[] args) {
        ConfigurableApplicationContext
                context = SpringApplication.run(Application.class, args);
        BookSearcher searcher =
                (BookSearcher) context.getBean(
                        "bookSearcher");
        System.out.println(searcher);
    }
}
