package com.dcrux.buran.queryDsl.queries.unfielded;

import com.dcrux.buran.queryDsl.indexingDef.IntIndexingDef;

/**
 * Buran.
 *
 * @author: ${USER} Date: 06.08.13 Time: 11:16
 */
public class IntCmp implements ISimpleQuery<Number, IntIndexingDef> {
    private NumCmp cmp;
    private Number rhs;

    public IntCmp(NumCmp cmp, Number rhs) {
        this.cmp = cmp;
        this.rhs = rhs;
    }

    @Override
    public boolean supports(IntIndexingDef indexingDef) {
        return true;
    }

    @Override
    public boolean matches(IntIndexingDef indexingDef, Number value) {
        switch (this.cmp) {
            case equal:
                return value.equals(this.rhs);
            case greater:
                return value.longValue() > this.rhs.longValue();
            case less:
                return value.longValue() < this.rhs.longValue();
            default:
                throw new IllegalArgumentException();
        }
    }

    @Override
    public boolean isImplementationDefined() {
        return true;
    }
}
