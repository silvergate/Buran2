package com.dcrux.buran.common.fields.getter;

/**
 * Buran.
 *
 * @author: ${USER} Date: 30.06.13 Time: 13:10
 */
public class FieldGetInt extends FieldGetPrim<Number> implements IUnfieldedDataGetter<Number> {
    public static final FieldGetInt SINGLETON = new FieldGetInt();

    private FieldGetInt() {
    }
}
