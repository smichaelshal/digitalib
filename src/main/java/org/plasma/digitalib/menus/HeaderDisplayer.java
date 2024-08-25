package org.plasma.digitalib.menus;

import lombok.RequiredArgsConstructor;
import org.plasma.digitalib.tasks.Task;

import java.util.Map;

@RequiredArgsConstructor
public class HeaderDisplayer implements Displayer {
    private final String header;

    public final void display(final Map<String, Task> tasks) {
        System.out.println(this.header);
        for  (String key : tasks.keySet()) {
            System.out.printf("%s - %s\n", key, tasks.get(key).getName());
        }
    }
}
