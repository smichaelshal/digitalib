package org.plasma.digitalib.borrower;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.plasma.digitalib.filters.BookIdentifierFilter;
import org.plasma.digitalib.models.Book;
import org.plasma.digitalib.models.BookIdentifier;
import org.plasma.digitalib.models.Borrowing;
import org.plasma.digitalib.models.BorrowingResult;
import org.plasma.digitalib.models.OrderRequest;
import org.plasma.digitalib.models.User;
import org.plasma.digitalib.storage.Storage;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class NotifierBookBorrower implements Borrower<BookIdentifier> {
    private final BorrowableItemNotifier<Book> notifier;
    private final Storage<Book> storage;
    private final BorrowingFactory borrowingFactory;

    public final BorrowingResult borrowItem(
            @NonNull final OrderRequest<BookIdentifier> request) {
        BookIdentifier bookIdentifier = request.getItemIdentifier();
        if (bookIdentifier == null) {
            log.warn("Get null bookIdentifier of borrow request");
            return BorrowingResult.INVALID_REQUEST;
        }

        List<Book> books = this.filterBooks(request);
        if (books.isEmpty()) {
            log.debug(
                    "No matching books at all "
                            + "were found for the borrow request");
            return BorrowingResult.NOT_EXIST;
        }

        List<Book> availableBooks = books.stream()
                .filter((book) -> !book.getIsBorrowed())
                .toList();
        if (availableBooks.isEmpty()) {
            log.debug(
                    "No matching books present were"
                            + " found for the borrow request: {}", request);
            return BorrowingResult.OUT_OF_STOCK;
        }

        Book book = availableBooks.get(0);
        boolean updateResult = this.updateBorrowBook(book, request);

        if (!updateResult) {
            return BorrowingResult.BORROWER_ERROR;
        }

        boolean notifierResult = this.notifier.add(book);
        if (!notifierResult) {
            log.error("Add notify of book failed: {}", book);
            this.deleteLastBorrowing(book);
            return BorrowingResult.BORROWER_ERROR;
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
            log.warn("Get null bookIdentifier of return request: {}", request);
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
                        log.debug(
                                "The book borrowed but its "
                                        + "borrowings is empty: {}",
                                book);
                        return false;
                    }

                    Borrowing lastBorrowing = borrowings.get(
                            borrowings.size() - 1);
                    return lastBorrowing.getUser().equals(user);
                })
                .toList();
        if (matchBooks.isEmpty()) {
            log.debug("Not found match books to request: {}", request);
            return false;
        }

        Book book = matchBooks.get(0);
        if (!this.notifier.delete(book)) {
            log.warn("Delete notify failed of book: {}", book);
        }

        return this.updateReturnBook(book);
    }

    private boolean updateBorrowBook(
            @NonNull final Book book,
            @NonNull final OrderRequest<BookIdentifier> request) {

        book.getBorrowings()
                .add(this.borrowingFactory.create(request.getUser()));
        book.setIsBorrowed(true);
        if (!this.storage.update(book.getId(), book)) {
            log.warn("Failed add borrowing to book: {}", book);
            book.setIsBorrowed(false);
        }

        log.debug("Success add borrowing to book: {}", book);
        return true;
    }

    private boolean updateReturnBook(
            @NonNull final Book book) {
        List<Borrowing> borrowings = book.getBorrowings();
        borrowings.get(borrowings.size() - 1)
                .setReturnTime(Optional.of(Instant.now()));
        book.setIsBorrowed(false);
        if (!this.storage.update(book.getId(), book)) {
            log.warn("Failed return borrowing to book: {}", book);
            book.setIsBorrowed(false);
            return false;
        }

        log.debug("Success return borrowing to book: {}", book);
        return true;
    }
}
