package org.plasma.digitalib.tasks;

import lombok.RequiredArgsConstructor;
import org.plasma.digitalib.models.BookIdentifier;
import org.plasma.digitalib.models.OrderRequest;
import org.plasma.digitalib.models.User;

@RequiredArgsConstructor
public class ConsoleCreatorBookIdentifierOrderRequest {
    private final ConsoleCreatorBookIdentifier creatorBookIdentifier;
    private final ConsoleUtils consoleUtils;

    public final OrderRequest<BookIdentifier> create() {
        String userId = this.consoleUtils.getNotEmptyParameter("user id");
        BookIdentifier bookIdentifier = this.creatorBookIdentifier.create();
        return new OrderRequest<>(new User(userId), bookIdentifier);
    }
}
