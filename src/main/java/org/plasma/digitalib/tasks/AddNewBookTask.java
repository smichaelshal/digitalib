package org.plasma.digitalib.tasks;

import lombok.NonNull;
import org.plasma.digitalib.adders.ItemAdder;
import org.plasma.digitalib.inputs.Input;
import org.plasma.digitalib.models.Book;
import org.plasma.digitalib.models.BookIdentifier;

public class AddNewBookTask extends Task {
    private final ItemAdder<Book> adder;
    private final ConsoleCreatorBookIdentifier creatorBookIdentifier;
    private final Input consoleUtils;

    public AddNewBookTask(
            @NonNull final String name,
            @NonNull final ItemAdder<Book> adder,
            @NonNull final ConsoleCreatorBookIdentifier creatorBookIdentifier,
            @NonNull final Input consoleUtils) {
        super(name);
        this.adder = adder;
        this.creatorBookIdentifier = creatorBookIdentifier;
        this.consoleUtils = consoleUtils;
    }

    @Override
    public final void run() {
        BookIdentifier bookIdentifier = this.creatorBookIdentifier.create();
        String genre = this.consoleUtils.getNotEmptyParameter(
                "genre book");
        String summary = this.consoleUtils.getNotEmptyParameter(
                "summary book");

        Book book = new Book(genre, summary, bookIdentifier);
        boolean adderResult = this.adder.add(book);
        if (adderResult) {
            System.out.println("success created book");
        } else {
            System.out.println("failed created book, try again");
        }
    }
}
