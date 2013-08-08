package com.dcrux.buran.query.indexingDef;

import com.dcrux.buran.common.fields.getter.FieldGetExists;
import com.dcrux.buran.common.fields.getter.IUnfieldedDataGetter;

/**
 * Buran.
 *
 * @author: ${USER} Date: 06.08.13 Time: 11:37
 */
public class ExistsIndexingDef implements IIndexingDef<Boolean> {

    private boolean requiredForIndex;

    public ExistsIndexingDef(boolean requiredForIndex) {
        this.requiredForIndex = requiredForIndex;
    }

    @Override
    public IUnfieldedDataGetter<Boolean> getDataGetter() {
        return FieldGetExists.SINGLETON;
    }

    @Override
    public boolean requiredForIndex() {
        return this.requiredForIndex;
    }
}
