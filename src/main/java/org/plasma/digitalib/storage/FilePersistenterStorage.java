package org.plasma.digitalib.storage;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.plasma.digitalib.dtos.BorrowableItem;

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


public class FilePersistenterStorage<T extends BorrowableItem & Serializable> implements Storage<T> {
    private final List<T> items;
    private final Path directoryPath;
    private final ObjectMapper objectMapper;


    public FilePersistenterStorage(List<T> items, Path directoryPath) {
        this.items = items;
        this.directoryPath = directoryPath;

        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.recover();
    }

    public boolean create(T item) {
        try {
            this.items.add(item);
            this.saveItem(item);
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
        this.saveItem(newItem);
    }

    private void saveItem(T item) {
        File file = Path.of(this.directoryPath.toString(), item.getId().toString()).toFile();
        try {
            this.objectMapper.writeValue(file, item);
        } catch (Exception e) {
            System.out.println(e.toString());
            // log
        }
    }

    private void recover() {
        try (Stream<Path> paths = Files.walk(this.directoryPath)) {
            paths.forEach(path -> {
                try {
                    this.items.add((T)objectMapper.readValue(path.toFile(), new TypeReference<T>() {}));
                } catch (IOException e) {
                    System.out.println(e.toString());
                    // log
                }
            });
        } catch (IOException e) {
            System.out.println(e.toString());
            // log
        }
    }
}
