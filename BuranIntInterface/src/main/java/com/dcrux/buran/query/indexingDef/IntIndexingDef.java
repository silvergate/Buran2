package com.dcrux.buran.query.indexingDef;

import com.dcrux.buran.common.fields.getter.FieldGetInt;
import com.dcrux.buran.common.fields.getter.IUnfieldedDataGetter;

import javax.annotation.Nullable;

/**
 * Buran.
 *
 * @author: ${USER} Date: 06.08.13 Time: 11:37
 */
public class IntIndexingDef implements IIndexingDef<Number> {

    private boolean requiredForIndex;
    private IUnfieldedDataGetter<Number> getter;

    public IntIndexingDef(boolean requiredForIndex) {
        this(requiredForIndex, null);
    }

    public static IntIndexingDef withGetter(boolean requiredForIndex,
            IUnfieldedDataGetter<Number> getter) {
        return new IntIndexingDef(requiredForIndex, getter);
    }

    public IntIndexingDef(boolean requiredForIndex, @Nullable IUnfieldedDataGetter<Number> getter) {
        this.requiredForIndex = requiredForIndex;
        this.getter = getter;
    }

    @Override
    public IUnfieldedDataGetter<Number> getDataGetter() {
        if (this.getter == null) {
            return FieldGetInt.SINGLETON;
        } else {
            return this.getter;
        }
    }

    @Override
    public boolean requiredForIndex() {
        return this.requiredForIndex;
    }
}
