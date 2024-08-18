package org.plasma.digitalib.borrower;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.plasma.digitalib.filters.BookIdentifierFilter;
import org.plasma.digitalib.models.*;

import org.plasma.digitalib.storage.FilePersistenterStorage;
import org.plasma.digitalib.storage.Storage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RequiredArgsConstructor
public class NotifierBookBorrower implements Borrower<BookIdentifier> {
    private final UpdaterBookBorrower updater;
    private final BorrowableItemNotifier<Book> notifier;
    private final Storage<Book> storage;
    private final Logger logger = LoggerFactory.getLogger(
            FilePersistenterStorage.class);

    public final BorrowingResult borrowItem(
            @NonNull final OrderRequest<BookIdentifier> request) {
        BookIdentifier bookIdentifier = request.getItemIdentifier();
        if (bookIdentifier == null) {
            return BorrowingResult.INVALID_REQUEST;
        }

        List<Book> books = this.filterBooks(request);
        if (books.isEmpty()) {
            return BorrowingResult.NOT_EXIST;
        }

        List<Book> availableBooks = books.stream()
                .filter((book) -> !book.getIsBorrowed())
                .toList();
        if (availableBooks.isEmpty()) {
            return BorrowingResult.OUT_OF_STOCK;
        }

        Book book = availableBooks.get(0);
        boolean updaterResult = this.updater.borrowItem(
                new ImmutablePair<>(book, request));
        if (!updaterResult) {
            return BorrowingResult.INVALID_REQUEST; // ???
        }

        boolean notifierResult = this.notifier.add(book);
        if (!notifierResult) {
            this.deleteLastBorrowing(book);
            return BorrowingResult.INVALID_REQUEST; // ???
        }

        return BorrowingResult.SUCCESS;
    }

    private List<Book> filterBooks(
            @NonNull final OrderRequest<BookIdentifier> request) {
        BookIdentifierFilter filter = new BookIdentifierFilter(
                request.getItemIdentifier());
        return this.storage.readAll(filter);
    }

    private void deleteLastBorrowing(final Book book) {
        List<Borrowing> borrowings = book.getBorrowings();
        borrowings.remove(borrowings.size() - 1);
        this.storage.update(book.getId(), book);
    }

    public final boolean returnItem(
            @NonNull final OrderRequest<BookIdentifier> request) {
        BookIdentifier bookIdentifier = request.getItemIdentifier();
        if (bookIdentifier == null) {
            return false;
        }
        User user = request.getUser();
        List<Book> books = this.filterBooks(request);
        List<Book> matchBooks = books.stream()
                .filter((book) -> {
                    if (!book.getIsBorrowed()) {
                        return false;
                    }

                    List<Borrowing> borrowings = book.getBorrowings();
                    if (borrowings.isEmpty()) {
                        return false;
                    }

                    Borrowing lastBorrowing = borrowings.get(
                            borrowings.size() - 1);
                    return lastBorrowing.getUser().equals(user);
                })
                .toList();
        if (matchBooks.isEmpty()) {
            return false;
        }

        Book book = matchBooks.get(0);
        if (!this.notifier.delete(book)) {
            logger.error("delete notify failed of {}", book.getId());
        }

        return this.updater.returnItem(new ImmutablePair<>(book, request));
    }
}
