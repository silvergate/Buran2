package com.dcrux.buran.common.fields.getter;

import com.dcrux.buran.common.classes.ClassId;
import com.dcrux.buran.common.fields.FieldIndex;
import com.dcrux.buran.common.fields.FieldTarget;
import com.dcrux.buran.common.fields.IFieldGetter;
import com.dcrux.buran.common.fields.IFieldTarget;

import java.io.Serializable;

/**
 * Buran.
 *
 * @author: ${USER} Date: 02.07.13 Time: 18:12
 */
public class SingleGet<TRetval extends Serializable> implements IFieldGetter<TRetval> {
    private IFieldTarget fieldIndex;
    private IUnfieldedDataGetter<TRetval> fieldGetter;

    public SingleGet(IFieldTarget fieldIndex, IUnfieldedDataGetter<TRetval> fieldGetter) {
        this.fieldIndex = fieldIndex;
        this.fieldGetter = fieldGetter;
    }

    private SingleGet() {
    }

    public static <TretvalLocal extends Serializable> SingleGet<TretvalLocal> c(ClassId classId,
            FieldIndex fieldIndex, IUnfieldedDataGetter<TretvalLocal> fieldGetter) {
        return new SingleGet<TretvalLocal>(FieldTarget.c(classId, fieldIndex), fieldGetter);
    }

    public static <TretvalLocal extends Serializable> SingleGet<TretvalLocal> c(
            IFieldTarget fieldIndex, IUnfieldedDataGetter<TretvalLocal> fieldGetter) {
        return new SingleGet<TretvalLocal>(fieldIndex, fieldGetter);
    }

    public IFieldTarget getFieldIndex() {
        return fieldIndex;
    }

    public IUnfieldedDataGetter<TRetval> getFieldGetter() {
        return fieldGetter;
    }
}
