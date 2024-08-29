package org.plasma.digitalib.inputs;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Scanner;

@RequiredArgsConstructor
public class ConsoleInput implements Input {
    private final Scanner scanner;

    public final String getNotEmptyParameter(
            @NonNull final String nameParameter) {
        while (true) {
            String parameter = this.getParameter(nameParameter);
            if (!parameter.isEmpty()) {
                return parameter;
            } else {
                System.out.printf("The %s cannot be empty%n", nameParameter);
            }
        }
    }

    public final String getParameter(@NonNull final String nameParameter) {
        System.out.printf("Enter the %s%n", nameParameter);
        return this.scanner.nextLine();
    }
}
