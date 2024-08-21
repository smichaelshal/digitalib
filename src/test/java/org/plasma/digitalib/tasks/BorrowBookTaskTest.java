package org.plasma.digitalib.tasks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.plasma.digitalib.borrower.Borrower;
import org.plasma.digitalib.inputs.Input;
import org.plasma.digitalib.models.BookIdentifier;
import org.plasma.digitalib.models.BorrowingResult;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BorrowBookTaskTest {
    private ConsoleCreatorBookIdentifierOrderRequest
            creatorBookIdentifierOrderRequest;

    @Mock
    Borrower<BookIdentifier> borrower;

    @Mock
    Input consoleInput;

    @BeforeEach
    void setup() {
        // Arrange
        MockitoAnnotations.initMocks(this);
        ConsoleCreatorBookIdentifier creatorBookIdentifier =
                new ConsoleCreatorBookIdentifier(consoleInput);

        this.creatorBookIdentifierOrderRequest =
                new ConsoleCreatorBookIdentifierOrderRequest(
                        creatorBookIdentifier, consoleInput);

        when(consoleInput.getNotEmptyParameter(any()))
                .thenReturn("book");
        when(borrower.borrowItem(any()))
                .thenReturn(BorrowingResult.SUCCESS);
        when(borrower.returnItem(any()))
                .thenReturn(true);
    }

    @Test
    void run_withStringInput_shouldCallBorrowItem() {
        // Arrange
        Task borrowTask = new BorrowBookTask("borrow book",
                this.creatorBookIdentifierOrderRequest, borrower);

        // Act
        borrowTask.run();

        // Assert
        verify(borrower).borrowItem(any());
    }

    @Test
    void run_withStringInput_shouldCallReturnItem() {
        // Arrange
        Task returnTask = new ReturnBookTask("borrow book",
                this.creatorBookIdentifierOrderRequest, borrower);

        // Act
        returnTask.run();

        // Assert
        verify(borrower).returnItem(any());
    }
}
