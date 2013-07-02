package com.dcrux.buran.commandBase;

/**
 * Buran.
 *
 * @author: ${USER} Date: 02.07.13 Time: 14:48
 */
public class UncheckedException extends Exception {
    private final Throwable wrapped;

    public UncheckedException(Throwable wrapped) {
        this.wrapped = wrapped;
    }

    public Throwable getWrapped() {
        return wrapped;
    }

    @Override
    public String toString() {
        return "UncheckedException{" +
                "wrapped=" + wrapped +
                '}';
    }
}
