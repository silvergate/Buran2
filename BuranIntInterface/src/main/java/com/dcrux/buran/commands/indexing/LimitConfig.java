package com.dcrux.buran.commands.indexing;

import java.io.Serializable;

/**
 * Buran.
 *
 * @author: ${USER} Date: 09.07.13 Time: 18:08
 */
@Deprecated
public class LimitConfig implements Serializable {
    private boolean returnPartialResults;
    private int limit;

    public LimitConfig(boolean returnPartialResults, int limit) {
        this.returnPartialResults = returnPartialResults;
        this.limit = limit;
    }

    private LimitConfig() {
    }

    public boolean isReturnPartialResults() {
        return returnPartialResults;
    }

    public int getLimit() {
        return limit;
    }
}
