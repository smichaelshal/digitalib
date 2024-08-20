package org.plasma.digitalib.storage;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.plasma.digitalib.models.BorrowableItem;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
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
                log.debug("failed to add item because failed save");
                this.items.remove(item);
                return false;
            }
        } catch (Exception e) {
            log.error("failed to add item", e);
            return false;
        }
        log.debug("success created {}", item);
        return true;
    }

    public final List<T> readAll(@NonNull final Predicate<T> filter) {
        log.debug("read all by filter: {}", filter);
        return this.items.stream()
                .filter(filter).
                collect(Collectors.toList());
    }

    public final boolean update(
            @NonNull final UUID id,
            @NonNull final T newItem) {

        for (int i = 0; i < this.items.size(); i++) {
            if (id.equals(this.items.get(i).getId())) {
                T oldItem = this.items.get(i);
                this.items.set(i, newItem);
                boolean saveResult = this.saveItem(newItem);
                if (!saveResult) {
                    log.debug("failed save update {}", oldItem);
                    this.items.set(i, oldItem);
                    return false;
                }
                log.debug("success updated {}", newItem);
                return true;
            }
        }
        log.debug("id not found: {}", id);
        return false;
    }

    private boolean saveItem(final T item) {
        File file = Path.of(this.directoryPath.toString(),
                item.getId().toString()).toFile();
        try {
            this.objectMapper.writeValue(file, item);
        } catch (Exception e) {
            log.error("save item failed: ", e);
            return false;
        }

        return true;
    }

    private void recover() {
        log.debug("start recover");
        try (Stream<Path> paths = Files.walk(this.directoryPath)
                .filter(Files::isRegularFile)) {
            paths.forEach(path -> {
                try {
                    T item = this.objectMapper.readValue(
                            path.toFile(),
                            new TypeReference<T>() {
                            });
                    this.items.add(item);
                    log.debug("success recover: {}", item);
                } catch (IOException e) {
                    log.error("failed recover item at: {}", path, e);
                }
            });
        } catch (IOException e) {
            log.error("failed recover storage from: {}", this.directoryPath, e);
        }
    }
}
