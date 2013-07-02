package com.dcrux.buran.common.fields.setter;

import com.dcrux.buran.common.fields.FieldIndex;
import com.dcrux.buran.common.fields.IDataSetter;

import java.util.HashMap;
import java.util.Map;

/**
 * Buran.
 *
 * @author: ${USER} Date: 29.06.13 Time: 16:56
 */
//TODO: Rename to DataSetter
public class DataSetter implements IDataSetter {

    public static DataSetter c(int index, IUnfieldedDataSetter setter) {
        return c(FieldIndex.c(index), setter);
    }

    public static DataSetter c(FieldIndex index, IUnfieldedDataSetter setter) {
        final DataSetter bs = new DataSetter();
        bs.add(index, setter);
        return bs;
    }

    public DataSetter add(int index, IUnfieldedDataSetter setter) {
        this.setterMap.put(FieldIndex.c(index), setter);
        return this;
    }

    public DataSetter add(FieldIndex index, IUnfieldedDataSetter setter) {
        this.setterMap.put(index, setter);
        return this;
    }

    private final Map<FieldIndex, IUnfieldedDataSetter> setterMap = new HashMap<>();

    public Map<FieldIndex, IUnfieldedDataSetter> getSetterMap() {
        return setterMap;
    }
}
