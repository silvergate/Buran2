package com.dcrux.buran.scripting.iface;

import com.sun.istack.internal.Nullable;

/**
 * Buran.
 *
 * @author: ${USER} Date: 04.07.13 Time: 17:30
 */
public interface ITypeState {
    @Nullable
    IType getType(VarName name);

    void setType(VarName name, IType type, boolean isFinal) throws ProgrammErrorException;

    void removeType(VarName name) throws ProgrammErrorException;

    void ret(IType retType) throws ProgrammErrorException;

    void jumpTo(ILineNumProvider jumpTo) throws ProgrammErrorException;

    void branch(ILineNumProvider jumpTo) throws ProgrammErrorException;

    IComplexityTracker getCompTracker();
}
