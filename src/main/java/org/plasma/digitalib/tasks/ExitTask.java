package org.plasma.digitalib.tasks;

public class ExitTask extends Task {
    public ExitTask(final String name) {
        super(name);
    }

    @Override
    public final void run() {
        System.exit(0);
    }
}
