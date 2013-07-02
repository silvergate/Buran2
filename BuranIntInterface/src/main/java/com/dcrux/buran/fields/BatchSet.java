package com.dcrux.buran.fields;

import com.dcrux.buran.fields.setter.IDataSetter;

import java.util.HashMap;
import java.util.Map;

/**
 * Buran.
 *
 * @author: ${USER} Date: 29.06.13 Time: 16:56
 */
public class BatchSet {

    public static BatchSet c(FieldIndex index, IDataSetter setter) {
        final BatchSet bs = new BatchSet();
        bs.add(index, setter);
        return bs;
    }

    public BatchSet add(FieldIndex index, IDataSetter setter) {
        this.setterMap.put(index, setter);
        return this;
    }

    private final Map<FieldIndex, IDataSetter> setterMap = new HashMap<>();

    public Map<FieldIndex, IDataSetter> getSetterMap() {
        return setterMap;
    }
}
