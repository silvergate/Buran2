package com.dcrux.buran.common.fields.getter;

import com.dcrux.buran.common.fields.FieldIndex;
import com.dcrux.buran.common.fields.IDataGetter;

import java.io.Serializable;

/**
 * Buran.
 *
 * @author: ${USER} Date: 02.07.13 Time: 18:12
 */
public class SingleGet<TRetval extends Serializable> implements IDataGetter<TRetval> {
    private final FieldIndex fieldIndex;
    private final IUnfieldedDataGetter<TRetval> fieldGetter;

    public SingleGet(FieldIndex fieldIndex, IUnfieldedDataGetter<TRetval> fieldGetter) {
        this.fieldIndex = fieldIndex;
        this.fieldGetter = fieldGetter;
    }

    public static <TretvalLocal extends Serializable> SingleGet<TretvalLocal> c(int fieldIndex,
            IUnfieldedDataGetter<TretvalLocal> fieldGetter) {
        return new SingleGet<TretvalLocal>(FieldIndex.c(fieldIndex), fieldGetter);
    }

    public static <TretvalLocal extends Serializable> SingleGet<TretvalLocal> c(
            FieldIndex fieldIndex, IUnfieldedDataGetter<TretvalLocal> fieldGetter) {
        return new SingleGet<TretvalLocal>(fieldIndex, fieldGetter);
    }

    public FieldIndex getFieldIndex() {
        return fieldIndex;
    }

    public IUnfieldedDataGetter<TRetval> getFieldGetter() {
        return fieldGetter;
    }
}