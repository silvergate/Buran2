package com.dcrux.buran.common.query.queries.unfielded;

import com.dcrux.buran.common.query.indexingDef.BoolIndexingDef;

/**
 * Buran.
 *
 * @author: ${USER} Date: 06.08.13 Time: 11:16
 */
public class BoolEq implements ISimpleQuery<Boolean, BoolIndexingDef> {
    private Boolean exists;

    public BoolEq(Boolean exists) {
        this.exists = exists;
    }

    @Override
    public boolean supports(BoolIndexingDef indexingDef) {
        return true;
    }

    @Override
    public boolean matches(BoolIndexingDef indexingDef, Boolean value) {
        return value.equals(this.exists);
    }

    @Override
    public boolean isImplementationDefined() {
        return true;
    }
}
