package org.plasma.digitalib.tasks;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public abstract class Task {
    private final String name;

    public abstract void run();
}
