package com.dcrux.buran.common.fields.getter;

import com.dcrux.buran.common.fields.FieldIndex;
import com.dcrux.buran.common.fields.IFieldGetter;

import java.util.HashMap;
import java.util.Map;

/**
 * Buran.
 *
 * @author: ${USER} Date: 01.07.13 Time: 00:47
 */
public class FieldGet implements IFieldGetter<FieldGetResult> {

    public static FieldGet c(int index, IUnfieldedDataGetter<?> getter) {
        return c(FieldIndex.c(index), getter);
    }

    public static FieldGet c(FieldIndex index, IUnfieldedDataGetter<?> getter) {
        final FieldGet fieldGet = new FieldGet();
        return fieldGet.add(index, getter);
    }

    private Map<FieldIndex, IUnfieldedDataGetter<?>> entries =
            new HashMap<FieldIndex, IUnfieldedDataGetter<?>>();

    public Map<FieldIndex, IUnfieldedDataGetter<?>> getEntries() {
        return entries;
    }

    public FieldGet add(int index, IUnfieldedDataGetter<?> getter) {
        return add(FieldIndex.c(index), getter);
    }

    public FieldGet add(FieldIndex index, IUnfieldedDataGetter<?> getter) {
        this.entries.put(index, getter);
        return this;
    }
}
