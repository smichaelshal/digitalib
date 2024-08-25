package org.plasma.digitalib.borrower;

import org.plasma.digitalib.models.Book;
import org.plasma.digitalib.models.BookIdentifier;
import org.plasma.digitalib.models.Borrowing;

import java.util.List;
import java.util.function.Consumer;

public final class BookExpiredPrinter implements Consumer<Book> {
    public void accept(final Book book) {
        List<Borrowing> borrowings = book.getBorrowings();
        if (borrowings.isEmpty()) {
            return;
        }

        BookIdentifier bookIdentifier = book.getBookIdentifier();
        System.out.printf("The book expired:\nName: %s\nAuthor: %s\nUser: %s\n",
                bookIdentifier.getName(),
                bookIdentifier.getAuthor(),
                borrowings.get(borrowings.size() - 1));
    }
}
