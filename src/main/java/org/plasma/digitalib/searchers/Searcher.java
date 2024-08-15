package org.plasma.digitalib.searchers;

import java.util.List;
import java.util.function.Function;

public interface Searcher <T>{
    List<T> search(Function<T, Boolean> filter);
}
