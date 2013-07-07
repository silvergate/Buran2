package com.dcrux.buran.scripting.iface;

import com.dcrux.buran.scripting.iface.types.AltType;
import com.sun.istack.internal.Nullable;

import java.util.Set;

/**
 * Buran.
 *
 * @author: ${USER} Date: 05.07.13 Time: 08:39
 */
public class TypeCombiner {
    public static IType<?> combine(IType<?> t1, IType<?> t2) {
        if (t1.equals(t2)) {
            return t1;
        }
        if (t1.getClass().equals(t2.getClass())) {
            return t1.combineWith(t2);
        } else {
            return AltType.c(t1, t2);
        }
    }

    @Nullable
    public static IType<?> combine(Set<IType<?>> typeSet) {
        if (typeSet.isEmpty()) {
            return null;
        }
        if (typeSet.size() == 1) {
            return typeSet.iterator().next();
        }
        return AltType.c(typeSet);
    }
}
