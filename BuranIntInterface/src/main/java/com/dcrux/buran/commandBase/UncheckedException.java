package com.dcrux.buran.commandBase;

/**
 * Buran.
 *
 * @author: ${USER} Date: 02.07.13 Time: 14:48
 */
public class UncheckedException extends Exception {
    private Throwable wrapped;

    public UncheckedException(Throwable wrapped) {
        this.wrapped = wrapped;
        wrapped.printStackTrace();
    }

    private UncheckedException() {
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
