package org.plasma.digitalib.tasks;

import org.apache.commons.lang3.StringUtils;
import org.plasma.digitalib.models.Book;
import org.plasma.digitalib.models.BookIdentifier;
import org.plasma.digitalib.models.Borrowing;
import org.plasma.digitalib.searchers.Searcher;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;


public class PrintBooksByFilterTask extends Task {
    private final Searcher<Book> searcher;
    private final Predicate<Book> filter;
    private final DateTimeFormatter timeFormatter;

    public PrintBooksByFilterTask(final String name,
                                  final Searcher<Book> searcher,
                                  final Predicate<Book> filter,
                                  final DateTimeFormatter timeFormatter) {
        super(name);
        this.searcher = searcher;
        this.filter = filter;
        this.timeFormatter = timeFormatter;
    }

    public final void run() {
        List<Book> books = this.searcher.search(this.filter);

        for (Book book : books) {
            System.out.println(this.formatBook(book));
        }

        if (books.isEmpty()) {
            System.out.println("No books matching your search were found");
        }
    }

    private String formatBook(final Book book) {
        BookIdentifier bookIdentifier = book.getBookIdentifier();

        String baseInfo = String.format("Name: %s\nAuthor: %s\n",
                bookIdentifier.getName(),
                bookIdentifier.getAuthor());
        String generalInfo = String.format("Enter time: %s%nIs borrowed:"
                        + "%s%n\n",
                this.formatTime(book.getEnteredTime()),
                book.getIsBorrowed());
        LinkedList<String> borrowings = new LinkedList<>();
        for (Borrowing borrowing : book.getBorrowings()) {
            borrowings.add(this.formatBorrowing(borrowing));
        }

        return baseInfo
                + generalInfo
                + StringUtils.join(borrowings, "\n");
    }

    private String formatBorrowing(final Borrowing borrowing) {

        return String.format(
                "User: %s\nBorrowing time: %s\nExpired time: %s\n%s\n",
                borrowing.getUser().getId(),
                this.formatTime(borrowing.getBorrowingTime()),
                this.formatTime(borrowing.getExpiredTime()),
                borrowing.getReturnTime().isPresent()
                        ? "Return time: "
                        + this.formatTime(borrowing.getReturnTime().get())
                        : "Not returned");
    }

    private String formatTime(final Instant time) {
        ZonedDateTime currentTime = time.atZone(ZoneId.systemDefault());
        return currentTime.format(this.timeFormatter);
    }
}
