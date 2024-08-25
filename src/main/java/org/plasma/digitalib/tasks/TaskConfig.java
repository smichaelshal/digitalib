package org.plasma.digitalib.tasks;

import org.plasma.digitalib.adders.ItemAdder;
import org.plasma.digitalib.borrower.Borrower;
import org.plasma.digitalib.inputs.Input;
import org.plasma.digitalib.models.Book;
import org.plasma.digitalib.models.BookIdentifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = AddNewBookTask.class)
public class TaskConfig {
    @Bean
    public AddNewBookTask addNewBookTask(
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
    public AddBookCopyTask addBookCopyTask(
            final ItemAdder<Book> adder,
            final ConsoleCreatorBookIdentifier creatorBookIdentifier) {
        return new AddBookCopyTask(
                "add copy book",
                adder,
                creatorBookIdentifier);
    }

    @Bean
    public BorrowBookTask borrowBookTask(
            final ConsoleCreatorBookIdentifierOrderRequest
                    consoleCreatorBookIdentifierOrderRequest,
            final Borrower<BookIdentifier> borrower) {
        return new BorrowBookTask(
                "borrow book",
                consoleCreatorBookIdentifierOrderRequest,
                borrower);
    }

    @Bean
    public ReturnBookTask returnBookTask(
            final ConsoleCreatorBookIdentifierOrderRequest
                    consoleCreatorBookIdentifierOrderRequest,
            final Borrower<BookIdentifier> borrower) {
        return new ReturnBookTask(
                "return book",
                consoleCreatorBookIdentifierOrderRequest,
                borrower);
    }

    @Bean
    public ConsoleCreatorBookIdentifier consoleCreatorBookIdentifier(
            final Input consoleInput
    ) {
        return new ConsoleCreatorBookIdentifier(consoleInput);
    }

    @Bean
    public ConsoleCreatorBookIdentifierOrderRequest
    consoleCreatorBookIdentifierOrderRequest(
            final ConsoleCreatorBookIdentifier creatorBookIdentifier,
            final Input consoleInput
    ) {
        return new ConsoleCreatorBookIdentifierOrderRequest(
                creatorBookIdentifier, consoleInput);
    }
}
