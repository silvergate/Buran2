package com.dcrux.buran.common.inRelations;

import java.io.Serializable;

/**
 * Buran.
 *
 * @author: ${USER} Date: 04.08.13 Time: 23:04
 */
public class InRelationResult<TResult extends Serializable> implements Serializable {
    private boolean limited;
    private TResult result;

    public InRelationResult(boolean limited, TResult result) {
        this.limited = limited;
        this.result = result;
    }

    private InRelationResult() {
    }

    public boolean isLimited() {
        return limited;
    }

    public TResult getResult() {
        if (result == null) {
            throw new IllegalArgumentException("No partial results available.");
        }
        return result;
    }
}
