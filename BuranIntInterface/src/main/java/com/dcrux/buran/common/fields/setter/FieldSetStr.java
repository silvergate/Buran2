package com.dcrux.buran.common.fields.setter;

import com.dcrux.buran.common.fields.typeDef.ITypeDef;
import com.dcrux.buran.common.fields.types.StringType;

/**
 * Buran.
 *
 * @author: ${USER} Date: 29.06.13 Time: 14:52
 */
public class FieldSetStr implements IUnfieldedDataSetter {

    private String value;

    public FieldSetStr(String value) {
        if (value == null) {
            throw new IllegalArgumentException("value==null");
        }
        this.value = value;
    }

    private FieldSetStr() {
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
