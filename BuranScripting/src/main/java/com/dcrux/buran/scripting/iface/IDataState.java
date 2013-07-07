package com.dcrux.buran.scripting.iface;

import com.sun.istack.internal.Nullable;

/**
 * Buran.
 *
 * @author: ${USER} Date: 04.07.13 Time: 18:25
 */
public interface IDataState {
    @Nullable
    Object getValue(VarName name);

    void setValue(VarName name, Object value);

    void removeValue(VarName name);

    void jumpTo(int lineNum);

    void ret(Object value);
}
