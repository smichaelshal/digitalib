package org.plasma.digitalib.borrower;

import java.time.Duration;

public interface UpdaterBorrower<T> {
    boolean borrowItem(T request, Duration borrowingDuration);
    boolean returnItem(T request);
}
