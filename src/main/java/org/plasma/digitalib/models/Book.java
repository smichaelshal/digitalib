package org.plasma.digitalib.models;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
public class Book extends BorrowableItem implements Serializable {
    private String genre;
    private String summary;
    private BookIdentifier bookIdentifier;

    public Book() {
        super(null, null, false, null);
    }

    public Book(
            final List<Borrowing> borrowings,
            final UUID id,
            final Instant enteredTime,
            final Boolean isBorrowed,
            final String genre,
            final String summary,
            final BookIdentifier bookIdentifier) {
        super(borrowings, enteredTime, isBorrowed, id);
        this.genre = genre;
        this.summary = summary;
        this.bookIdentifier = bookIdentifier;
    }

    public Book(
            final String genre,
            final String summary,
            final BookIdentifier bookIdentifier) {
        super(new LinkedList<Borrowing>(),
                Instant.now(),
                false,
                UUID.randomUUID());
        this.genre = genre;
        this.summary = summary;
        this.bookIdentifier = bookIdentifier;
    }

    public Book(
            final String genre,
            final String summary,
            final BookIdentifier bookIdentifier,
            final UUID id) {
        super(new LinkedList<Borrowing>(),
                Instant.now(),
                false,
                id);
        this.genre = genre;
        this.summary = summary;
        this.bookIdentifier = bookIdentifier;
    }

    public Book(final String genre,
                final String summary,
                final BookIdentifier bookIdentifier,
                final Boolean isBorrowed) {
        super(new LinkedList<Borrowing>(),
                Instant.now(),
                isBorrowed,
                UUID.randomUUID());
        this.genre = genre;
        this.summary = summary;
        this.bookIdentifier = bookIdentifier;
    }
}
