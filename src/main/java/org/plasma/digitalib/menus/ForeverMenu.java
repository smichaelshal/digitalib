package org.plasma.digitalib.menus;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ForeverMenu implements Menu {
    private final Menu menu;

    public final void run() {
        while (true) {
            this.menu.run();
        }
    }
}
