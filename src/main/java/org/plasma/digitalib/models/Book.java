package org.plasma.digitalib.models;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class Book extends BorrowableItem implements Serializable {
    private String genre;
    private String summary;
    private BookIdentifier bookIdentifier;

    public Book() {
        super(null, null, false, null);
    }

    public Book(final List<Borrowing> borrowingsBook,
                final UUID idBook,
                final Instant enteredTimeBook,
                final Boolean isBorrowed,
                final String genreBook,
                final String summaryBook,
                final BookIdentifier bookIdentifierBook) {
        super(borrowingsBook, enteredTimeBook, isBorrowed, idBook);
        this.genre = genreBook;
        this.summary = summaryBook;
        this.bookIdentifier = bookIdentifierBook;
    }

    public Book(final String genreBook,
                final String summaryBook,
                final BookIdentifier bookIdentifierBook) {
        super(new LinkedList<Borrowing>(),
                Instant.now(),
                false,
                UUID.randomUUID());
        this.genre = genreBook;
        this.summary = summaryBook;
        this.bookIdentifier = bookIdentifierBook;
    }

    public Book(final String genreBook,
                final String summaryBook,
                final BookIdentifier bookIdentifierBook,
                final Boolean isBorrowed) {
        super(new LinkedList<Borrowing>(),
                Instant.now(),
                isBorrowed,
                UUID.randomUUID());
        this.genre = genreBook;
        this.summary = summaryBook;
        this.bookIdentifier = bookIdentifierBook;
    }
}
