package com.dcrux.buran.common.fields.getter;

import com.dcrux.buran.common.fields.IFieldGetter;

/**
 * Buran.
 *
 * @author: ${USER} Date: 01.07.13 Time: 00:47
 */
@Deprecated
public class FieldGetAll implements IFieldGetter<FieldGetResult> {

    public static final FieldGetAll SINGLETON = new FieldGetAll();

    private FieldGetAll() {
    }


    public static FieldGetAll c() {
        return SINGLETON;
    }
}
