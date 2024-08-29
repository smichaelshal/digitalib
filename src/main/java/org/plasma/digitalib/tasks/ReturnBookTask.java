package org.plasma.digitalib.tasks;

import lombok.NonNull;
import org.plasma.digitalib.borrower.Borrower;
import org.plasma.digitalib.models.BookIdentifier;
import org.plasma.digitalib.models.OrderRequest;

public class ReturnBookTask extends Task {
    private final ConsoleCreatorBookIdentifierOrderRequest
            creatorBookIdentifierOrderRequest;
    private final Borrower<BookIdentifier> borrower;

    public ReturnBookTask(@NonNull final String name,
                          @NonNull final
                          ConsoleCreatorBookIdentifierOrderRequest
                                  creatorBookIdentifierOrderRequest,
                          @NonNull final Borrower<BookIdentifier> borrower) {
        super(name);
        this.creatorBookIdentifierOrderRequest =
                creatorBookIdentifierOrderRequest;
        this.borrower = borrower;
    }

    public final void run() {
        OrderRequest<BookIdentifier> orderRequest =
                this.creatorBookIdentifierOrderRequest.create();
        boolean returnResult = this.borrower.returnItem(orderRequest);
        if (returnResult) {
            System.out.println("The book has been successfully returned");
        } else {
            System.out.println("The request is invalid, try again");
        }
    }
}
