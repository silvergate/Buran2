package com.dcrux.buran.scripting.functions;

import com.dcrux.buran.scripting.iface.*;

/**
 * Buran.
 *
 * @author: ${USER} Date: 04.07.13 Time: 17:40
 */
public class FunGet<TRetType extends IType<?>> extends FunctionDeclaration<TRetType> {

    private VarName varName;

    public FunGet(VarName varName) {
        this.varName = varName;
    }

    private FunGet() {
    }

    public VarName getVarName() {
        return varName;
    }

    public static <TRetTypeLocal extends IType<?>> FunGet<TRetTypeLocal> c(VarName varName,
            Class<TRetTypeLocal> clazz) {
        return new FunGet<TRetTypeLocal>(varName);
    }

    @Override
    public AllocType<TRetType> getMeta(ITypeState state) throws ProgrammErrorException {
        final IType<?> type = state.getType(getVarName());
        if (type == null) {
            throw new ProgrammErrorException("Variable " + getVarName() + " is not set.");
        }

        return (AllocType<TRetType>) state.getCompTracker().alloc(type);
    }
}
