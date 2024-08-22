package org.plasma.digitalib.borrower;

import org.plasma.digitalib.models.Book;
import org.plasma.digitalib.models.BookIdentifier;

import java.util.function.Consumer;

public final class BookPrinter implements Consumer<Book> {
    public void accept(final Book book) {
        BookIdentifier bookIdentifier = book.getBookIdentifier();
        System.out.printf("%s\n%s\n%s\n%s\n%n",
                bookIdentifier.getName(),
                bookIdentifier.getAuthor(),
                book.getGenre(),
                book.getSummary());
    }
}
