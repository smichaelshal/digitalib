package org.plasma.digitalib.storage;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.NonNull;
import org.plasma.digitalib.models.BorrowableItem;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class FilePersistenterStorage<T extends BorrowableItem & Serializable>
        implements Storage<T> {
    private final List<T> items;
    private final Path directoryPath;
    private final ObjectMapper objectMapper;

    public FilePersistenterStorage(
            final List<T> borrowableItems,
            final Path directoryPersistenterPath,
            final ObjectMapper objectMapper) {
        this.items = borrowableItems;
        this.directoryPath = directoryPersistenterPath;
        this.objectMapper = objectMapper;
        this.recover();
    }

    public final boolean create(final T item) {
        if (item == null) {
            return false;
        }

        try {
            this.items.add(item);
            this.saveItem(item);
        } catch (ClassCastException | NullPointerException e) {
            return false;
        }
        return true;
    }
//    @NonNull
    public final List<T> readAll(final Function<T, Boolean> filter) {
        if (filter != null) {
            return this.items.stream()
                    .filter(filter::apply).
                    collect(Collectors.toList());
        }
        return new LinkedList<T>();
    }

    // oldItem -> id >>>>
    public final boolean update(final T oldItem, final T newItem) {
        if (oldItem == null || newItem == null) {
            return false;
        }
        boolean isFind = false;
        for (int i = 0; i < this.items.size(); i++) {
            if (oldItem.getId().equals(this.items.get(i).getId())) {
                this.items.set(i, newItem);
                isFind = true;
                break;
            }
        }
        this.saveItem(newItem);
        return isFind;
    }

    private void saveItem(final T item) {
        File file = Path.of(this.directoryPath.toString(),
                item.getId().toString()).toFile();
        try {
            this.objectMapper.writeValue(file, item);
        } catch (Exception e) {
            // log
        }
    }

    private void recover() {
        try (Stream<Path> paths = Files.walk(this.directoryPath)) {
            paths.forEach(path -> {
                try {
                    this.items.add(this.objectMapper.readValue(
                            path.toFile(),
                            new TypeReference<T>() {
                            }));
                } catch (IOException e) {
                    // log
                }
            });
        } catch (IOException e) {
            // log
        }
    }
}
