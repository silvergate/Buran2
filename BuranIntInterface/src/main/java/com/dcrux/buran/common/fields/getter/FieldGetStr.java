package com.dcrux.buran.common.fields.getter;

/**
 * Buran.
 *
 * @author: ${USER} Date: 30.06.13 Time: 13:10
 */
public class FieldGetStr extends FieldGetPrim<String> implements IUnfieldedDataGetter<String> {
    public static final FieldGetStr SINGLETON = new FieldGetStr();

    private FieldGetStr() {
    }
}
