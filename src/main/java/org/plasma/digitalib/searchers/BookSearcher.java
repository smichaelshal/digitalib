package org.plasma.digitalib.searchers;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.plasma.digitalib.models.Book;
import org.plasma.digitalib.storage.Storage;

import java.util.List;
import java.util.function.Function;

@RequiredArgsConstructor
public class BookSearcher implements Searcher<Book> {
    private final Storage<Book> storage;

    public final List<Book> search(@NonNull final Function<Book, Boolean> filter) {
        return this.storage.readAll(filter);
    }
}
