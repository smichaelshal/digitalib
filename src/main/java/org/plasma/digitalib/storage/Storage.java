package org.plasma.digitalib.storage;

import org.plasma.digitalib.models.BorrowableItem;

import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

public interface Storage<T extends BorrowableItem> {
    boolean create(T item);

    List<T> readAll(Predicate<T> filter);

    boolean update(UUID id, T newItem);
}
