package com.dcrux.buran.common.fields.setter;

import com.dcrux.buran.common.fields.typeDef.ITypeDef;
import com.dcrux.buran.common.fields.types.IntegerType;

/**
 * Buran.
 *
 * @author: ${USER} Date: 29.06.13 Time: 14:52
 */
public class SetInt implements IUnfieldedDataSetter {

    private final Number value;

    public SetInt(Number value) {
        this.value = value;
    }

    public Number getValue() {
        return value;
    }

    public static SetInt c(final Number value) {
        return new SetInt(value);
    }

    @Override
    public boolean canApplyTo(ITypeDef dataType) {
        return dataType instanceof IntegerType;
    }
}
