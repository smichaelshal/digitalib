package org.plasma.digitalib.configs;

import org.plasma.digitalib.adders.ItemAdder;
import org.plasma.digitalib.borrower.Borrower;
import org.plasma.digitalib.inputs.Input;
import org.plasma.digitalib.models.Book;
import org.plasma.digitalib.models.BookIdentifier;
import org.plasma.digitalib.tasks.AddBookCopyTask;
import org.plasma.digitalib.tasks.AddNewBookTask;
import org.plasma.digitalib.tasks.BorrowBookTask;
import org.plasma.digitalib.tasks.ConsoleCreatorBookIdentifier;
import org.plasma.digitalib.tasks.ConsoleCreatorBookIdentifierOrderRequest;
import org.plasma.digitalib.tasks.ExitTask;
import org.plasma.digitalib.tasks.ReturnBookTask;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = AddNewBookTask.class)
public class TaskConfig {

    @Bean
    public static AddNewBookTask addNewBookTask(
            final ItemAdder<Book> adder,
            final ConsoleCreatorBookIdentifier creatorBookIdentifier,
            final Input consoleUtils) {
        return new AddNewBookTask(
                "add new book",
                adder,
                creatorBookIdentifier,
                consoleUtils);
    }

    @Bean
    public static AddBookCopyTask addBookCopyTask(
            final ItemAdder<Book> adder,
            final ConsoleCreatorBookIdentifier creatorBookIdentifier) {
        return new AddBookCopyTask(
                "add copy book",
                adder,
                creatorBookIdentifier);
    }

    @Bean
    public static BorrowBookTask borrowBookTask(
            final ConsoleCreatorBookIdentifierOrderRequest
                    consoleCreatorBookIdentifierOrderRequest,
            final Borrower<BookIdentifier> borrower) {
        return new BorrowBookTask(
                "borrow book",
                consoleCreatorBookIdentifierOrderRequest,
                borrower);
    }

    @Bean
    public static ReturnBookTask returnBookTask(
            final ConsoleCreatorBookIdentifierOrderRequest
                    consoleCreatorBookIdentifierOrderRequest,
            final Borrower<BookIdentifier> borrower) {
        return new ReturnBookTask(
                "return book",
                consoleCreatorBookIdentifierOrderRequest,
                borrower);
    }

    @Bean
    public static ExitTask exitTask() {
        return new ExitTask("exit");
    }

    @Bean
    public static ConsoleCreatorBookIdentifier consoleCreatorBookIdentifier(
            final Input consoleInput) {
        return new ConsoleCreatorBookIdentifier(consoleInput);
    }

    @Bean
    public static ConsoleCreatorBookIdentifierOrderRequest
    consoleCreatorBookIdentifierOrderRequest(
            final ConsoleCreatorBookIdentifier creatorBookIdentifier,
            final Input consoleInput) {
        return new ConsoleCreatorBookIdentifierOrderRequest(
                creatorBookIdentifier, consoleInput);
    }
}
