package org.plasma.digitalib.configs;

import org.plasma.digitalib.inputs.ConsoleInput;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.Scanner;

@Configuration
@ComponentScan(basePackageClasses = ConsoleInput.class)
public class InputConfig {
    /**
     * @return
     */
    @Bean
    public ConsoleInput consoleInput() {
        Scanner scanner = new Scanner(System.in);
        return new ConsoleInput(scanner);
    }
}

