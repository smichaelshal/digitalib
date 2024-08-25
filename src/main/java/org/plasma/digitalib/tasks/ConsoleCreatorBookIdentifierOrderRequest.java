package org.plasma.digitalib.tasks;

import lombok.RequiredArgsConstructor;
import org.plasma.digitalib.inputs.Input;
import org.plasma.digitalib.models.BookIdentifier;
import org.plasma.digitalib.models.OrderRequest;
import org.plasma.digitalib.models.User;

@RequiredArgsConstructor
public class ConsoleCreatorBookIdentifierOrderRequest {
    private final ConsoleCreatorBookIdentifier creatorBookIdentifier;
    private final Input consoleInput;

    public final OrderRequest<BookIdentifier> create() {
        String userId = this.consoleInput.getNotEmptyParameter("user id");
        BookIdentifier bookIdentifier = this.creatorBookIdentifier.create();
        return new OrderRequest<>(new User(userId), bookIdentifier);
    }
}
