package com.dcrux.buran.scripting.iface;

import java.io.Serializable;

/**
 * Buran.
 *
 * @author: ${USER} Date: 04.07.13 Time: 17:30
 */
public interface IType<TJavaType> extends Serializable {
    IType<TJavaType> combineWith(IType<?> other);

    int getMemoryMaxMemoryRequirement();
}
