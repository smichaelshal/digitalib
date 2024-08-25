package menus;

import org.plasma.digitalib.tasks.Task;

import java.util.Map;

public interface Displayer {
    void display(Map<String, Task> tasks);
}
