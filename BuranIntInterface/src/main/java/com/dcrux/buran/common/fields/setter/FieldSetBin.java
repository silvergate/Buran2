package com.dcrux.buran.common.fields.setter;

import com.dcrux.buran.common.fields.typeDef.ITypeDef;
import com.dcrux.buran.common.fields.types.BinaryType;

/**
 * Buran.
 *
 * @author: ${USER} Date: 29.06.13 Time: 14:52
 */
public class FieldSetBin implements IUnfieldedDataSetter {

    private byte[] value;

    public FieldSetBin(byte[] value) {
        this.value = value;
    }

    private FieldSetBin() {
    }

    public byte[] getValue() {
        return value;
    }

    public static FieldSetBin c(final byte[] value) {
        return new FieldSetBin(value);
    }

    @Override
    public boolean canApplyTo(ITypeDef dataType) {
        return dataType instanceof BinaryType;
    }
}
