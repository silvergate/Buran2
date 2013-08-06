package com.dcrux.buran.common.fields.getter;

/**
 * Buran.
 *
 * @author: ${USER} Date: 30.06.13 Time: 13:10
 */
public class FieldGetExists implements IUnfieldedDataGetter<Boolean> {
    public static final FieldGetExists SINGLETON = new FieldGetExists();

    protected FieldGetExists() {
    }
}
