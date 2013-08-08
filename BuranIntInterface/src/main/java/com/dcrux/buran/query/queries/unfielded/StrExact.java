package com.dcrux.buran.query.queries.unfielded;

import com.dcrux.buran.query.indexingDef.StrExactIndexingDef;

/**
 * Buran.
 *
 * @author: ${USER} Date: 06.08.13 Time: 11:16
 */
public class StrExact implements ISimpleQuery<String, StrExactIndexingDef> {
    private String rhs;

    public StrExact(String rhs) {
        this.rhs = rhs;
    }

    @Override
    public boolean supports(StrExactIndexingDef indexingDef) {
        return true;
    }

    @Override
    public boolean matches(StrExactIndexingDef indexingDef, String value) {
        final int len = indexingDef.getNumberOfChars();
        if (rhs.length() > len) {
            /* Too long, no possiblility for match */
            return false;
        }
        final String valShort;
        if (value.length() > len) {
            valShort = value.substring(0, len);
        } else {
            valShort = value;
        }

        return value.equals(value);
    }

    @Override
    public boolean isImplementationDefined() {
        return true;
    }
}
