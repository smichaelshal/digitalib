package org.plasma.digitalib.tasks;

import lombok.RequiredArgsConstructor;
import org.plasma.digitalib.models.BookIdentifier;

import java.util.Scanner;

@RequiredArgsConstructor
public class ConsoleCreatorBookIdentifier {
    private final ConsoleUtils consoleUtils;

    public final BookIdentifier create() {
        String bookName = this.consoleUtils.getNotEmptyParameter(
                "book name");
        String bookAuthor = this.consoleUtils.getNotEmptyParameter(
                "book author");
        return new BookIdentifier(bookName, bookAuthor);
    }


}
