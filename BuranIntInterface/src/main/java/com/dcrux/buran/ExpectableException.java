package com.dcrux.buran;

/**
 * Buran.
 *
 * @author: ${USER} Date: 30.06.13 Time: 23:06
 */
public class ExpectableException extends Exception {
    public ExpectableException() {
    }

    public ExpectableException(String message) {
        super(message);
    }

    public ExpectableException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExpectableException(Throwable cause) {
        super(cause);
    }

    public ExpectableException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
