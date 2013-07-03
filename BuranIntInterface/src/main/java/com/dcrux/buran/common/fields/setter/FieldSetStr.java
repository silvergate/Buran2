package com.dcrux.buran.common.fields.setter;

import com.dcrux.buran.common.fields.typeDef.ITypeDef;
import com.dcrux.buran.common.fields.types.StringType;

/**
 * Buran.
 *
 * @author: ${USER} Date: 29.06.13 Time: 14:52
 */
public class FieldSetStr implements IUnfieldedDataSetter {

    private final String value;

    public FieldSetStr(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static FieldSetStr c(final String value) {
        return new FieldSetStr(value);
    }

    @Override
    public boolean canApplyTo(ITypeDef dataType) {
        return dataType instanceof StringType;
    }
}