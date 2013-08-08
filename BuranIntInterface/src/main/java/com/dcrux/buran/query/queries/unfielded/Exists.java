package com.dcrux.buran.query.queries.unfielded;

import com.dcrux.buran.query.indexingDef.ExistsIndexingDef;

/**
 * Buran.
 *
 * @author: ${USER} Date: 06.08.13 Time: 11:16
 */
public class Exists implements ISimpleQuery<Boolean, ExistsIndexingDef> {
    private Boolean exists;

    public Exists(Boolean exists) {
        this.exists = exists;
    }

    @Override
    public boolean supports(ExistsIndexingDef indexingDef) {
        return true;
    }

    @Override
    public boolean matches(ExistsIndexingDef indexingDef, Boolean value) {
        return value.equals(this.exists);
    }

    @Override
    public boolean isImplementationDefined() {
        return true;
    }
}
