package com.dcrux.buran.scripting.iface.types;

import com.dcrux.buran.scripting.iface.IType;

/**
 * Buran.
 *
 * @author: ${USER} Date: 04.07.13 Time: 17:36
 */
public final class VoidType implements IType<Object> {
    public static final VoidType SINGLETON = new VoidType();

    private VoidType() {
    }

    @Override
    public String toString() {
        return "VoidType{}";
    }

    @Override
    public IType<Object> combineWith(IType<?> other) {
        return this;
    }

    @Override
    public int getMemoryMaxMemoryRequirement() {
        return 1;
    }
}
