package org.plasma.digitalib.configs;

import org.plasma.digitalib.borrower.BaseBorrowingFactory;
import org.plasma.digitalib.borrower.BookExpiredPrinter;
import org.plasma.digitalib.borrower.BorrowableItemNotifier;
import org.plasma.digitalib.borrower.BorrowingFactory;
import org.plasma.digitalib.borrower.NotifierBookBorrower;
import org.plasma.digitalib.borrower.TimerBorrowableItemNotifier;
import org.plasma.digitalib.models.Book;
import org.plasma.digitalib.storage.Storage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Consumer;


@Configuration
@ComponentScan(basePackageClasses = TimerBorrowableItemNotifier.class)
@ComponentScan(basePackageClasses = NotifierBookBorrower.class)
public class BorrowerConfig {
    /**
     * @return
     */
    @Bean
    public ScheduledExecutorService scheduledExecutorServiceBookNotifier() {
        return Executors.newScheduledThreadPool(10);
    }

    /**
     * @return
     */
    @Bean
    public Consumer<Book> notifyConsumer() {
        return new BookExpiredPrinter();
    }

    /**
     * @param scheduledExecutorService
     * @param storage
     * @param notifyConsumer
     * @return
     */
    @Bean
    public TimerBorrowableItemNotifier<Book> bookTimerBorrowableItemNotifier(
            final ScheduledExecutorService scheduledExecutorService,
            final Storage<Book> storage,
            final Consumer<Book> notifyConsumer) {
        return new TimerBorrowableItemNotifier<>(scheduledExecutorService,
                storage,
                notifyConsumer);
    }

    /**
     * @param notifier
     * @param storage
     * @param borrowingFactory
     * @return
     */
    @Bean
    public NotifierBookBorrower notifierBookBorrower(
            final BorrowableItemNotifier<Book> notifier,
            final Storage<Book> storage,
            final BorrowingFactory borrowingFactory
    ) {
        return new NotifierBookBorrower(notifier, storage, borrowingFactory);
    }

    /**
     * @return
     */
    @Bean
    public BorrowingFactory borrowingFactory() {
        return new BaseBorrowingFactory(Duration.of(30, ChronoUnit.SECONDS));
    }
}
