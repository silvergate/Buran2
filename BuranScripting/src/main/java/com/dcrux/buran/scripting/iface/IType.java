package com.dcrux.buran.scripting.iface;

/**
 * Buran.
 *
 * @author: ${USER} Date: 04.07.13 Time: 17:30
 */
public interface IType<TJavaType> {
    IType<TJavaType> combineWith(IType<?> other);

    int getMemoryMaxMemoryRequirement();
}
