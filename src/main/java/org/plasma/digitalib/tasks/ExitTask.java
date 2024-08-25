package org.plasma.digitalib.tasks;

public class ExitTask extends Task {
    public ExitTask(String name) {
        super(name);
    }

    @Override
    public void run() {
        System.exit(0);
    }
}
