package com.dcrux.buran.common.fields.setter;

import com.dcrux.buran.common.fields.IFieldSetter;

/**
 * Buran.
 *
 * @author: ${USER} Date: 29.06.13 Time: 16:56
 */
public class FieldRemoveAll implements IFieldSetter {

    public static final FieldRemoveAll SINGLETON = new FieldRemoveAll();

    public static FieldRemoveAll c() {
        return SINGLETON;
    }

    private FieldRemoveAll() {
    }
}
