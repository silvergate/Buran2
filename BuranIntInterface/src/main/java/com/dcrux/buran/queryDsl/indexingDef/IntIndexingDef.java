package com.dcrux.buran.queryDsl.indexingDef;

import com.dcrux.buran.common.fields.getter.FieldGetInt;
import com.dcrux.buran.common.fields.getter.IUnfieldedDataGetter;

/**
 * Buran.
 *
 * @author: ${USER} Date: 06.08.13 Time: 11:37
 */
public class IntIndexingDef implements IIndexingDef<Number> {

    private boolean requiredForIndex;

    public IntIndexingDef(boolean requiredForIndex) {
        this.requiredForIndex = requiredForIndex;
    }

    @Override
    public IUnfieldedDataGetter<Number> getDataGetter() {
        return FieldGetInt.SINGLETON;
    }

    @Override
    public boolean requiredForIndex() {
        return this.requiredForIndex;
    }
}
