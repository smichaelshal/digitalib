package org.plasma.digitalib.tasks;

import lombok.NonNull;
import org.plasma.digitalib.borrower.Borrower;
import org.plasma.digitalib.models.BookIdentifier;
import org.plasma.digitalib.models.BorrowingResult;
import org.plasma.digitalib.models.OrderRequest;

public class BorrowBookTask extends Task {
    private final ConsoleCreatorBookIdentifierOrderRequest
            creatorBookIdentifierOrderRequest;
    private final Borrower<BookIdentifier> borrower;

    public BorrowBookTask(@NonNull final String name,
                          @NonNull
                          final ConsoleCreatorBookIdentifierOrderRequest
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
        BorrowingResult borrowingResult =
                this.borrower.borrowItem(orderRequest);

        switch (borrowingResult) {
            case INVALID_REQUEST:
                System.out.println("The request was invalid, try again");
                break;
            case NOT_EXIST:
                System.out.println("The book is not in the library"
                        + ", try another book");
                break;
            case OUT_OF_STOCK:
                System.out.println("The book is not currently in the library"
                        + ", all copies have already been borrowed");
                break;
            case SUCCESS:
                System.out.println("The book has been borrowed successfully");
                break;
            default:
                System.out.println("Error, try again");
        }
    }
}
