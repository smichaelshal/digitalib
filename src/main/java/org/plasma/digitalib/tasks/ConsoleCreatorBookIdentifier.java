package org.plasma.digitalib.tasks;

import lombok.RequiredArgsConstructor;
import org.plasma.digitalib.models.BookIdentifier;

import java.util.Scanner;

@RequiredArgsConstructor
public class ConsoleCreatorBookIdentifier {
    private final Scanner scanner;

    public final BookIdentifier create() {
        String bookName = this.getNotEmptyParameter("book name");
        String bookAuthor = this.getNotEmptyParameter("book author");
        return new BookIdentifier(bookName, bookAuthor);
    }

    private String getNotEmptyParameter(final String nameParameter) {
        while (true) {
            System.out.printf("Enter the %s%n", nameParameter);
            String parameter = this.scanner.nextLine();
            if (!parameter.isEmpty()) {
                return parameter;
            } else {
                System.out.printf("The %s cannot be empty%n", nameParameter);
            }
        }
    }
}
