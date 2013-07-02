package com.dcrux.buran.fields.types;

/**
 * Buran.
 *
 * @author: ${USER} Date: 29.06.13 Time: 16:22
 */
public class StringType implements IType {

    // TODO: Maxlen, min len

    @Override
    public boolean isValid(Object javaData) {
        return javaData instanceof String;
    }
}
