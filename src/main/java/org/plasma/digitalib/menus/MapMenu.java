package org.plasma.digitalib.menus;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.plasma.digitalib.inputs.Input;
import org.plasma.digitalib.tasks.Task;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class MapMenu implements Menu {
    private final Displayer displayer;
    private final Map<String, Task> tasks;
    private final Input input;

    public final void run() {
        this.displayer.display(this.tasks);
        String key = this.input.get();
        if (tasks.containsKey(key)) {
            try {
                this.tasks.get(key).run();
            } catch (Exception e) {
                log.error("The task {} running failed:",
                        this.tasks.get(key), e);
            }
        } else {
            System.out.println("Key not found, try again");
        }
    }
}
