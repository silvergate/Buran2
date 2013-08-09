package com.dcrux.buran.common.fields.getter;

import com.dcrux.buran.common.classes.ClassId;

import java.util.EnumSet;
import java.util.HashSet;

/**
 * Buran.
 *
 * @author: ${USER} Date: 30.06.13 Time: 13:10
 */
public class FieldGetClassIds implements IUnfieldedDataGetter<HashSet<ClassId>> {
    public static enum Type {
        primary,
        secondaries
    }

    private EnumSet<Type> types;
    public static EnumSet<Type> DEFAULT = EnumSet.allOf(Type.class);

    private FieldGetClassIds() {
        this.types = DEFAULT;
    }

    public FieldGetClassIds(EnumSet<Type> types) {
        if (types.isEmpty()) {
            throw new IllegalArgumentException("types.isEmpty()");
        }
        this.types = types;
    }

    public static FieldGetClassIds c() {
        return new FieldGetClassIds(DEFAULT);
    }

    public static FieldGetClassIds primary() {
        return new FieldGetClassIds(EnumSet.of(Type.primary));
    }

    public static FieldGetClassIds secondaries() {
        return new FieldGetClassIds(EnumSet.of(Type.secondaries));
    }

    public EnumSet<Type> getTypes() {
        return types;
    }
}
