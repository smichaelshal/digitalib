package org.plasma.digitalib;

import org.plasma.digitalib.menus.ForeverMenu;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Application {
    public static void main(final String[] args) {
        try {
            ConfigurableApplicationContext
                    context = SpringApplication.run(Application.class, args);
            ForeverMenu foreverMenu =
                    (ForeverMenu) context.getBean(
                            "foreverMenu");
            foreverMenu.run();
        } catch (Exception e) {
            System.out.println("");
        }
    }
}
