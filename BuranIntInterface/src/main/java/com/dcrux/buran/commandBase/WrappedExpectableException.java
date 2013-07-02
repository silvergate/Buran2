package com.dcrux.buran.commandBase;

/**
 * Buran.
 *
 * @author: ${USER} Date: 02.07.13 Time: 15:03
 */
public class WrappedExpectableException extends Exception {
    private final Exception wrappedException;

    public WrappedExpectableException(Exception wrappedException) {
        this.wrappedException = wrappedException;
    }
}
