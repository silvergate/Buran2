package com.dcrux.buran.common.fields.getter;

import com.dcrux.buran.common.fields.IFieldGetter;
import com.dcrux.buran.common.fields.IFieldTarget;

import java.util.HashMap;
import java.util.Map;

/**
 * Buran.
 *
 * @author: ${USER} Date: 01.07.13 Time: 00:47
 */
public class FieldGet implements IFieldGetter<FieldGetResult> {

    public static FieldGet c(IFieldTarget index, IUnfieldedDataGetter<?> getter) {
        final FieldGet fieldGet = new FieldGet();
        return fieldGet.add(index, getter);
    }

    private Map<IFieldTarget, IUnfieldedDataGetter<?>> entries =
            new HashMap<IFieldTarget, IUnfieldedDataGetter<?>>();

    public Map<IFieldTarget, IUnfieldedDataGetter<?>> getEntries() {
        return entries;
    }

    public FieldGet add(IFieldTarget index, IUnfieldedDataGetter<?> getter) {
        this.entries.put(index, getter);
        return this;
    }
}
