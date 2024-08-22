package org.plasma.digitalib;

import org.plasma.digitalib.borrower.TimerBorrowableItemNotifier;
import org.plasma.digitalib.models.Book;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Application {
    public static void main(final String[] args) {
        ConfigurableApplicationContext
                context = SpringApplication.run(Application.class, args);
        TimerBorrowableItemNotifier<Book> notifier =
                (TimerBorrowableItemNotifier) context.getBean(
                        "bookTimerBorrowableItemNotifier");
    }
}
