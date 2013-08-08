package com.dcrux.buran.common.fields.setter;

import com.dcrux.buran.common.classes.ClassId;
import com.dcrux.buran.common.fields.FieldIndex;
import com.dcrux.buran.common.fields.FieldTarget;
import com.dcrux.buran.common.fields.IFieldSetter;
import com.dcrux.buran.common.fields.IFieldTarget;

import java.util.HashMap;
import java.util.Map;

/**
 * Buran.
 *
 * @author: ${USER} Date: 29.06.13 Time: 16:56
 */
public class FieldSetter implements IFieldSetter {

    public static FieldSetter c() {
        final FieldSetter bs = new FieldSetter();
        return bs;
    }

    public static FieldSetter c(ClassId classId, FieldIndex index, IUnfieldedDataSetter setter) {
        return c(FieldTarget.c(classId, index), setter);
    }

    public static FieldSetter c(IFieldTarget index, IUnfieldedDataSetter setter) {
        final FieldSetter bs = new FieldSetter();
        bs.add(index, setter);
        return bs;
    }

    public FieldSetter add(IFieldTarget index, IUnfieldedDataSetter setter) {
        this.setterMap.put(index, setter);
        return this;
    }

    public FieldSetter add(ClassId classId, FieldIndex index, IUnfieldedDataSetter setter) {
        add(FieldTarget.c(classId, index), setter);
        return this;
    }

    private Map<IFieldTarget, IUnfieldedDataSetter> setterMap =
            new HashMap<IFieldTarget, IUnfieldedDataSetter>();

    public Map<IFieldTarget, IUnfieldedDataSetter> getSetterMap() {
        return setterMap;
    }
}
