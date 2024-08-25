package org.plasma.digitalib.tasks;

import lombok.NonNull;
import org.plasma.digitalib.adders.ItemAdder;
import org.plasma.digitalib.models.Book;
import org.plasma.digitalib.models.BookIdentifier;

public class AddBookCopyTask extends Task {
    private final ItemAdder<Book> adder;
    private final ConsoleCreatorBookIdentifier creatorBookIdentifier;

    public AddBookCopyTask(
            @NonNull final String name,
            @NonNull final ItemAdder<Book> adder,
            @NonNull final ConsoleCreatorBookIdentifier creatorBookIdentifier) {
        super(name);
        this.adder = adder;
        this.creatorBookIdentifier = creatorBookIdentifier;
    }

    @Override
    public final void run() {
        BookIdentifier bookIdentifier = this.creatorBookIdentifier.create();
        Book book = new Book("", "", bookIdentifier);
        boolean adderResult = this.adder.add(book);
        if (adderResult) {
            System.out.println("success created book");
        } else {
            System.out.println("failed created book, try again");
        }
    }
}
