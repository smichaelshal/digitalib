package org.plasma.digitalib;

import org.plasma.digitalib.menus.ForeverMenu;
import org.plasma.digitalib.menus.MapMenu;
import org.plasma.digitalib.tasks.ReturnBookTask;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Application {
    public static void main(final String[] args) {
        ConfigurableApplicationContext
                context = SpringApplication.run(Application.class, args);
        ForeverMenu foreverMenu =
                (ForeverMenu) context.getBean(
                        "foreverMenu");
        System.out.println(foreverMenu);

        foreverMenu.run();
    }
}
