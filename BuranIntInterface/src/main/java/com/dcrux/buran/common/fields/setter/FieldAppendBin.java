package com.dcrux.buran.common.fields.setter;

import com.dcrux.buran.common.fields.typeDef.ITypeDef;
import com.dcrux.buran.common.fields.types.BinaryType;

/**
 * Buran.
 *
 * @author: ${USER} Date: 29.06.13 Time: 14:52
 */
public class FieldAppendBin implements IUnfieldedDataSetter {

    private final byte[] value;

    public FieldAppendBin(byte[] value) {
        this.value = value;
    }

    public byte[] getValue() {
        return value;
    }

    public static FieldAppendBin c(final byte[] value) {
        return new FieldAppendBin(value);
    }

    @Override
    public boolean canApplyTo(ITypeDef dataType) {
        return dataType instanceof BinaryType;
    }
}
