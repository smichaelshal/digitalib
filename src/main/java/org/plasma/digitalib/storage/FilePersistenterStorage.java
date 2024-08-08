package org.plasma.digitalib.storage;

import org.plasma.digitalib.dtos.BorrowableItem;

import java.io.Serializable;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;


public class FilePersistenterStorage<T extends BorrowableItem & Serializable> implements Storage<T> {
    private final List<T> items;
    private final Path directoryPath;


    public FilePersistenterStorage(List<T> items, Path directoryPath) {
        this.items = items;
        this.directoryPath = directoryPath;
    }

    public boolean create(T item) {
        try {
            this.items.add(item);
        } catch (ClassCastException | NullPointerException e) {
            return false;
        }
        return true;
    }

    public List<T> readAll(Function<T, Boolean> filter) {
        if (filter == null) {
            return this.items.stream().filter(filter::apply).collect(Collectors.toList());
        }
        return new LinkedList<T>();
    }

    public void update(T oldItem, T newItem) {
        if (oldItem == null || newItem == null) {
            return;
        }
        for (int i = 0; i < this.items.size(); i++) {
            if (oldItem.getId() == newItem.getId()) {
                this.items.set(i, newItem);
            }
        }
    }
}
