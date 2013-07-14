package com.dcrux.buran.common.fields.getter;

import java.io.Serializable;

/**
 * Buran.
 *
 * @author: ${USER} Date: 30.06.13 Time: 13:10
 */
public class FieldGetPrim<TType extends Serializable> implements IUnfieldedDataGetter<TType> {
    public static final FieldGetPrim SINGLETON = new FieldGetPrim();

    protected FieldGetPrim() {
    }
}
