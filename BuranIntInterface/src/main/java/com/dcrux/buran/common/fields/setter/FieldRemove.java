package com.dcrux.buran.common.fields.setter;

import com.dcrux.buran.common.fields.typeDef.ITypeDef;

/**
 * Buran.
 *
 * @author: ${USER} Date: 29.06.13 Time: 14:52
 */
public class FieldRemove implements IUnfieldedDataSetter {

    public static final FieldRemove SINGLETON = new FieldRemove();

    private FieldRemove() {
    }

    public static FieldRemove c() {
        return SINGLETON;
    }

    @Override
    public boolean canApplyTo(ITypeDef dataType) {
        return true;
    }
}
