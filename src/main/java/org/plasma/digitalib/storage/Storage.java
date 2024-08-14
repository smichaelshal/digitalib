package org.plasma.digitalib.storage;

import java.util.List;
import java.util.function.Function;

public interface Storage<T> {
    boolean create(T item);

    List<T> readAll(Function<T, Boolean> filter);

    void update(T oldItem, T newItem);
}