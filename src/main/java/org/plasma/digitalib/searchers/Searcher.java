package org.plasma.digitalib.searchers;

import java.util.List;
import java.util.function.Predicate;

public interface Searcher<T> {
    List<T> search(Predicate<T> filter);
}
