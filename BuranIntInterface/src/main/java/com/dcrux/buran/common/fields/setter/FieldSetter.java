package com.dcrux.buran.common.fields.setter;

import com.dcrux.buran.common.fields.FieldIndex;
import com.dcrux.buran.common.fields.IFieldSetter;

import java.util.HashMap;
import java.util.Map;

/**
 * Buran.
 *
 * @author: ${USER} Date: 29.06.13 Time: 16:56
 */
public class FieldSetter implements IFieldSetter {

    public static FieldSetter c(int index, IUnfieldedDataSetter setter) {
        return c(FieldIndex.c(index), setter);
    }

    public static FieldSetter c(FieldIndex index, IUnfieldedDataSetter setter) {
        final FieldSetter bs = new FieldSetter();
        bs.add(index, setter);
        return bs;
    }

    public FieldSetter add(int index, IUnfieldedDataSetter setter) {
        this.setterMap.put(FieldIndex.c(index), setter);
        return this;
    }

    public FieldSetter add(FieldIndex index, IUnfieldedDataSetter setter) {
        this.setterMap.put(index, setter);
        return this;
    }

    private final Map<FieldIndex, IUnfieldedDataSetter> setterMap =
            new HashMap<FieldIndex, IUnfieldedDataSetter>();

    public Map<FieldIndex, IUnfieldedDataSetter> getSetterMap() {
        return setterMap;
    }
}
