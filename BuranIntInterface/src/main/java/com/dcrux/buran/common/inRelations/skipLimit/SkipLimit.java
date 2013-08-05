package com.dcrux.buran.common.inRelations.skipLimit;

import java.io.Serializable;

/**
 * Buran.
 *
 * @author: ${USER} Date: 04.08.13 Time: 23:03
 */
public class SkipLimit implements Serializable {
    private int skipCount;
    private int limit;
    private boolean returnPartialResults;

    public static final int LIMIT_MAX = Short.MAX_VALUE;
    public static final int SKIP_MAX = Short.MAX_VALUE;

    public SkipLimit(int skipCount, int limit, boolean returnPartialResults) {
        if (skipCount > SKIP_MAX) {
            throw new IllegalArgumentException("skipCount>SKIP_MAX");
        }
        if (limit > LIMIT_MAX) {
            throw new IllegalArgumentException("limit>LIMIT_MAX");
        }
        this.skipCount = skipCount;
        this.limit = limit;
        this.returnPartialResults = returnPartialResults;
    }

    private SkipLimit() {
    }

    public int getSkipCount() {
        return skipCount;
    }

    public int getLimit() {
        return limit;
    }

    public boolean isReturnPartialResults() {
        return returnPartialResults;
    }
}
