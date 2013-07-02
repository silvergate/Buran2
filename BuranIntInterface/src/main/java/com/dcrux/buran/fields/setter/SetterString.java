package com.dcrux.buran.fields.setter;

import com.dcrux.buran.fields.types.IType;
import com.dcrux.buran.fields.types.StringType;

/**
 * Buran.
 *
 * @author: ${USER} Date: 29.06.13 Time: 14:52
 */
public class SetterString implements IDataSetter {

    private final String value;

    public SetterString(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static SetterString c(final String value) {
        return new SetterString(value);
    }

    @Override
    public boolean canApplyTo(IType dataType) {
        return dataType instanceof StringType;
    }
}
