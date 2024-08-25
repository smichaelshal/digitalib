package org.plasma.digitalib.menus;

import lombok.RequiredArgsConstructor;
import org.plasma.digitalib.inputs.Input;
import org.plasma.digitalib.tasks.Task;

import java.util.Map;

@RequiredArgsConstructor
public class MapMenu implements Menu {
    private final Displayer displayer;
    private final Map<String, Task> tasks;
    private final Input input;

    public final void run() {
        this.displayer.display(this.tasks);
        String key = this.input.get();
        if (tasks.containsKey(key)) {
            this.tasks.get(key).run();
        } else {
            System.out.println("Key not found, try again");
        }
    }
}
