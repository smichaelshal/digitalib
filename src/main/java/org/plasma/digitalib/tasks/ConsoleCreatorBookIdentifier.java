package org.plasma.digitalib.tasks;

import lombok.RequiredArgsConstructor;
import org.plasma.digitalib.inputs.Input;
import org.plasma.digitalib.models.BookIdentifier;

@RequiredArgsConstructor
public class ConsoleCreatorBookIdentifier {
    private final Input consoleInput;

    public final BookIdentifier create() {
        String bookName = this.consoleInput.getNotEmptyParameter(
                "book name");
        String bookAuthor = this.consoleInput.getNotEmptyParameter(
                "book author");
        return new BookIdentifier(bookName, bookAuthor);
    }
}
