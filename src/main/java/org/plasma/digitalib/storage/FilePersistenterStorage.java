package org.plasma.digitalib.storage;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import org.plasma.digitalib.models.BorrowableItem;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class FilePersistenterStorage<T extends BorrowableItem & Serializable>
        implements Storage<T> {
    private final List<T> items;
    private final Path directoryPath;
    private final ObjectMapper objectMapper;

    public FilePersistenterStorage(
            final List<T> items,
            final Path directoryPath,
            final ObjectMapper objectMapper) {
        this.items = items;
        this.directoryPath = directoryPath;
        this.objectMapper = objectMapper;
        this.recover();
    }

    public final boolean create(@NonNull final T item) {
        try {
            this.items.add(item);
            if (!this.saveItem(item)) {
                this.items.remove(item);
                return false;
            }
        } catch (Exception e) {
            // log
            return false;
        }
        return true;
    }

    public final List<T> readAll(@NonNull final Function<T, Boolean> filter) {
        return this.items.stream()
                .filter(filter::apply).
                collect(Collectors.toList());
    }

    public final boolean update(
            @NonNull final UUID id,
            @NonNull final T newItem) {

        boolean isFind = false;
        for (int i = 0; i < this.items.size(); i++) {
            if (id.equals(this.items.get(i).getId())) {
                this.items.set(i, newItem);
                isFind = true;
                break;
            }
        }
        this.saveItem(newItem);
        return isFind;
    }

    private boolean saveItem(final T item) {
        File file = Path.of(this.directoryPath.toString(),
                item.getId().toString()).toFile();
        try {
            this.objectMapper.writeValue(file, item);
        } catch (Exception e) {
            // log
            return false;
        }
        return true;
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
