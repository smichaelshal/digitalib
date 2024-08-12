package org.plasma.digitalib.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Getter
public class Book extends BorrowableItem implements Serializable {
    private String genre;
    private String summary;
    private BookIdentifier bookIdentifier;

    public Book(List<Borrowing> borrowings, UUID id, Instant enteredTime, boolean isBorrowed, String genre,
                String summary, BookIdentifier bookIdentifier) {
        super(borrowings, enteredTime, isBorrowed, id);
        this.genre = genre;
        this.summary = summary;
        this.bookIdentifier = bookIdentifier;
    }

//    public Book(String genre, String summary, BookIdentifier bookIdentifier) {
//        super(new LinkedList<Borrowing>(), Instant.now(), false, UUID.randomUUID());
//        this.genre = genre;
//        this.summary = summary;
//        this.bookIdentifier = bookIdentifier;
//    }
}
