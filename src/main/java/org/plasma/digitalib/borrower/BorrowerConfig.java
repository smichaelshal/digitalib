package org.plasma.digitalib.borrower;

import org.plasma.digitalib.models.Book;
import org.plasma.digitalib.storage.Storage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Consumer;


@Configuration
@ComponentScan(basePackageClasses = TimerBorrowableItemNotifier.class)
public class BorrowerConfig {
    @Bean
    public ScheduledExecutorService scheduledExecutorServiceBookNotifier() {
        return Executors.newScheduledThreadPool(10);
    }

    @Bean
    public Consumer<Book> notifyConsumer() {
        return new BookPrinter();
    }

    @Bean
    public TimerBorrowableItemNotifier<Book> bookTimerBorrowableItemNotifier(
            final ScheduledExecutorService scheduledExecutorService,
            final Storage<Book> storage,
            final Consumer<Book> notifyConsumer) {
        return new TimerBorrowableItemNotifier<>(scheduledExecutorService,
                storage,
                notifyConsumer);
    }
}
