package org.plasma.digitalib.dtos;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.LinkedList;
import java.util.UUID;

@Getter
public class Book extends BorrowableItem implements Serializable {
    private final String genre;
    private final String summary;
    private final BookIdentifier bookIdentifier;

    public Book(String genre, String summary, BookIdentifier bookIdentifier) {
        super(new LinkedList<Borrowing>(), Instant.now(), false, UUID.randomUUID());
        this.genre = genre;
        this.summary = summary;
        this.bookIdentifier = bookIdentifier;
    }
}
