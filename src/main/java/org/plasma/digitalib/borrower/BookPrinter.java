package org.plasma.digitalib.borrower;

import org.plasma.digitalib.dtos.Book;
import org.plasma.digitalib.dtos.BookIdentifier;

import java.util.function.Consumer;

public class BookPrinter implements Consumer<Book> {
    public void accept(Book book) {
        BookIdentifier bookIdentifier = book.getBookIdentifier();
        System.out.println(String.format("%s\n%s\n%s\n%s\n",
                bookIdentifier.getName(),
                bookIdentifier.getAuthor(),
                book.getGenre(),
                book.getSummary()));
    }
}
