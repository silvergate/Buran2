package com.dcrux.buran.commands.indexing;

/**
 * Buran.
 *
 * @author: ${USER} Date: 09.07.13 Time: 18:08
 */
public class LimitConfig {
    private final boolean returnPartialResults;
    private final int limit;

    public LimitConfig(boolean returnPartialResults, int limit) {
        this.returnPartialResults = returnPartialResults;
        this.limit = limit;
    }

    public boolean isReturnPartialResults() {
        return returnPartialResults;
    }

    public int getLimit() {
        return limit;
    }
}
