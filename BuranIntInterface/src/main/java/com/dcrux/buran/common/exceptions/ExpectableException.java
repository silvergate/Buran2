package com.dcrux.buran.common.exceptions;

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

}
