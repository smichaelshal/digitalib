package org.plasma.digitalib.borrower;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.plasma.digitalib.models.Book;
import org.plasma.digitalib.models.BookIdentifier;
import org.plasma.digitalib.models.Borrowing;
import org.plasma.digitalib.models.OrderRequest;
import org.plasma.digitalib.storage.Storage;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class UpdaterBookBorrower implements UpdaterBorrower
        <Pair<Book, OrderRequest<BookIdentifier>>> {
    private final Storage<Book> storage;
    private final Duration borrowingDuration;

    public final boolean borrowItem(
            @NonNull final Pair<Book, OrderRequest<BookIdentifier>> request) {
        Book book = request.getLeft();
        OrderRequest<BookIdentifier> order = request.getRight();

        Instant borrowingTime = Instant.now();
        Instant expiredTime = Instant.now().plus(borrowingDuration);

        book.getBorrowings().add(new Borrowing(
                order.getUser(),
                borrowingTime,
                Optional.empty(),
                expiredTime));

        return storage.update(book.getId(), book);
    }

    public final boolean returnItem(
            @NonNull final Pair<Book, OrderRequest<BookIdentifier>> request) {
        Book book = request.getLeft();

        List<Borrowing> borrowings = book.getBorrowings();
        borrowings.get(borrowings.size() - 1)
                .setReturnTime(Optional.of(Instant.now()));

        return storage.update(book.getId(), book);
    }
}
