package org.plasma.digitalib.borrower;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.plasma.digitalib.filters.BookIdentifierFilter;
import org.plasma.digitalib.models.Book;
import org.plasma.digitalib.models.BookIdentifier;
import org.plasma.digitalib.models.Borrowing;
import org.plasma.digitalib.models.BorrowingResult;
import org.plasma.digitalib.models.OrderRequest;
import org.plasma.digitalib.models.User;
import org.plasma.digitalib.storage.FilePersistenterStorage;
import org.plasma.digitalib.storage.Storage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class NotifierBookBorrower implements Borrower<BookIdentifier> {
    private final UpdaterBookBorrower updater;
    private final BorrowableItemNotifier<Book> notifier;
    private final Storage<Book> storage;
    private final Duration borrowingDuration;


    public final BorrowingResult borrowItem(
            @NonNull final OrderRequest<BookIdentifier> request) {
        BookIdentifier bookIdentifier = request.getItemIdentifier();
        if (bookIdentifier == null) {
            log.info("get null bookIdentifier of borrow request");
            return BorrowingResult.INVALID_REQUEST;
        }

        List<Book> books = this.filterBooks(request);
        if (books.isEmpty()) {
            log.info(
                    "No matching books at all "
                            + "were found for the borrow request");
            return BorrowingResult.NOT_EXIST;
        }

        List<Book> availableBooks = books.stream()
                .filter((book) -> !book.getIsBorrowed())
                .toList();
        if (availableBooks.isEmpty()) {
            log.info(
                    "No matching books present were"
                            + " found for the borrow request");
            return BorrowingResult.OUT_OF_STOCK;
        }

        Book book = availableBooks.get(0);
        boolean updaterResult = this.updater.borrowItem(
                new ImmutablePair<>(book, request), this.borrowingDuration);
        if (!updaterResult) {
            return BorrowingResult.INVALID_REQUEST; // ???
        }

        boolean notifierResult = this.notifier.add(book);
        if (!notifierResult) {
            log.info("notifier failed");
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
            log.info("get null bookIdentifier of return request");
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
            log.info("delete notify failed of {}", book.getId());
        }

        return this.updater.returnItem(new ImmutablePair<>(book, request));
    }
}
