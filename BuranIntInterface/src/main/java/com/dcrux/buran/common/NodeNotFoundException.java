package com.dcrux.buran.common;

/**
 * Buran.
 *
 * @author: ${USER} Date: 30.06.13 Time: 23:06
 */
public class NodeNotFoundException extends ExpectableException {

    private final boolean wrongVersion;

    public NodeNotFoundException(String message, boolean wrongVersion) {
        super(message);
        this.wrongVersion = wrongVersion;
    }

    public NodeNotFoundException(boolean wrongVersion) {
        this.wrongVersion = wrongVersion;
    }

    public boolean isWrongVersion() {
        return wrongVersion;
    }
}
