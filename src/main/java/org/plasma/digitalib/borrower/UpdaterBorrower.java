package org.plasma.digitalib.borrower;

public interface UpdaterBorrower<T> {
    boolean borrowItem(T request);
    boolean returnItem(T request);
}
