package org.plasma.digitalib.tasks;

import lombok.NonNull;
import org.plasma.digitalib.adders.StorageBookAdder;
import org.plasma.digitalib.models.Book;
import org.plasma.digitalib.models.BookIdentifier;

public class AddNewBookTask extends Task {
    private final StorageBookAdder adder;
    private final ConsoleCreatorBookIdentifier creatorBookIdentifier;
    private final ConsoleUtils consoleUtils;

    public AddNewBookTask(
            @NonNull final String name,
            @NonNull final StorageBookAdder adder,
            @NonNull final ConsoleCreatorBookIdentifier creatorBookIdentifier,
            @NonNull final ConsoleUtils consoleUtils) {
        super(name);
        this.adder = adder;
        this.creatorBookIdentifier = creatorBookIdentifier;
        this.consoleUtils = consoleUtils;
    }

    @Override
    public final void run() {
        BookIdentifier bookIdentifier = this.creatorBookIdentifier.create();
        String genre = this.consoleUtils.getParameter(
                "genre book");
        String summary = this.consoleUtils.getParameter(
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
