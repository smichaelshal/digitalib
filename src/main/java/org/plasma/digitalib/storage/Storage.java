package org.plasma.digitalib.storage;

import org.plasma.digitalib.models.BorrowableItem;

import java.util.List;
import java.util.UUID;
import java.util.function.Function;

public interface Storage<T extends BorrowableItem> {
    boolean create(T item);

    List<T> readAll(Function<T, Boolean> filter);

    boolean update(UUID id, T newItem);
}
