package org.plasma.digitalib.tasks;

import lombok.NonNull;
import org.plasma.digitalib.adders.StorageBookAdder;

public class AddNewBookTask extends Task {
    private final StorageBookAdder adder;

    public AddNewBookTask(
            @NonNull final String name,
            @NonNull final StorageBookAdder adder) {
        super(name);
        this.adder = adder;
    }

    @Override
    public void run() {

    }
}
