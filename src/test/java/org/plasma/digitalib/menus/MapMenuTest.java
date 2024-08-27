package org.plasma.digitalib.menus;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.plasma.digitalib.inputs.Input;
import org.plasma.digitalib.menus.Displayer;
import org.plasma.digitalib.menus.MapMenu;
import org.plasma.digitalib.menus.Menu;
import org.plasma.digitalib.tasks.Task;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class MapMenuTest {
    private Menu menu;
    private String key;

    @Mock
    Task task;

    @Mock
    Displayer displayer;

    @Mock
    Input input;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.key = "key";
        Map<String, Task> tasks = new HashMap<>();
        tasks.put(this.key, task);
        this.menu = new MapMenu(displayer, tasks, input);
    }


    @Test
    void run_withExistKey_shouldCallTaskRun() {
        // Arrange
        when(input.get()).thenReturn(key);

        // Act
        this.menu.run();

        // Assert
        verify(task).run();
    }

    @Test
    void run_withNotExistKey_shouldNotCallTaskRun() {
        // Arrange
        when(input.get()).thenReturn(key + "_");

        // Act
        this.menu.run();

        // Assert
        verify(task, times(0)).run();
    }
}