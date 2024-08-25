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
    private final String directoryPath;
    private final ObjectMapper objectMapper;


    public FilePersistenterStorage(
            final List<T> items,
            final String directoryPath,
            final ObjectMapper objectMapper) {
        this.items = items;
        this.directoryPath = directoryPath;
        this.objectMapper = objectMapper;
        this.recover();
        this.createDirecotry();
    }

    public final boolean create(@NonNull final T item) {
        try {
            this.items.add(item);
            if (!this.saveItem(item)) {
                log.error("Failed to add item because failed save: {}", item);
                this.items.remove(item);
                return false;
            }

        } catch (Exception e) {
            log.error("Failed to add item: {}", item, e);
            return false;
        }

        log.debug("Success created {}", item);
        return true;
    }

    public final List<T> readAll(@NonNull final Predicate<T> filter) {
        log.debug("Read all by filter: {}", filter);
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
                    log.warn("Failed save update {}", oldItem);
                    this.items.set(i, oldItem);
                    return false;
                }
                log.debug("Success updated to item {}: {}", id, newItem);
                return true;
            }
        }

        log.debug("Failed to update because id not found: {}", id);
        return false;
    }

    private void createDirecotry() {
        File directory = new File(this.directoryPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    private boolean saveItem(final T item) {
        File file = Path.of(this.directoryPath,
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
        log.debug("Start recovery");
        try (Stream<Path> paths = Files.walk(Path.of(this.directoryPath))
                .filter(Files::isRegularFile)) {
            paths.forEach(path -> {
                try {
                    T item = this.objectMapper.readValue(
                            path.toFile(),
                            new TypeReference<T>() {});
                    this.items.add(item);
                    log.debug("Success recover item: {}", item);
                } catch (IOException e) {
                    log.error("Failed recover item at: {}", path, e);
                }
            });
        } catch (IOException e) {
            log.error("Failed recover storage from: {}"
                            + "reading from the directory failed",
                    this.directoryPath, e);
        }
    }
}
