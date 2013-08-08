package com.dcrux.buran.query.queries.unfielded;

import java.io.Serializable;

/**
 * Buran.
 *
 * @author: ${USER} Date: 08.08.13 Time: 14:51
 */
public interface IConstantIndicator extends Serializable {
    /**
     * Returns <code>true</code> if this query is a constant (usually machine generated) query
     * (often reused).
     *
     * @return true or false.
     */
    boolean isConstantQuery();
}
