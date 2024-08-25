package org.plasma.digitalib.filters;

import org.plasma.digitalib.models.Book;

import java.util.function.Predicate;

public class AllBookFilter implements Predicate<Book> {
    public final boolean test(final Book book) {
        return true;
    }
}
