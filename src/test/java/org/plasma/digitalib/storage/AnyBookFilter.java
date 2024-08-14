package org.plasma.digitalib.storage;

import org.plasma.digitalib.dtos.Book;

import java.util.function.Function;

public class AnyBookFilter implements Function<Book, Boolean> {
    public Boolean apply(Book book) {
        return true;
    }
}
