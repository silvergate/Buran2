package com.dcrux.buran.scripting.functions;

import com.dcrux.buran.scripting.iface.*;

import java.text.MessageFormat;

/**
 * Buran.
 *
 * @author: ${USER} Date: 04.07.13 Time: 17:40
 */
public class FunGet<TRetType extends IType<?>> extends FunctionDeclaration<TRetType> {

    private final VarName varName;

    public FunGet(VarName varName) {
        this.varName = varName;
    }

    public VarName getVarName() {
        return varName;
    }

    public static <TRetTypeLocal extends IType<?>> FunGet<TRetTypeLocal> c(VarName varName,
            Class<TRetTypeLocal> clazz) {
        return new FunGet<>(varName);
    }

    @Override
    public AllocType<TRetType> getMeta(ITypeState state) throws ProgrammErrorException {
        final IType<?> type = state.getType(getVarName());
        if (type == null) {
            throw new ProgrammErrorException(
                    MessageFormat.format("Variable {0} is not set.", getVarName()));
        }

        return (AllocType<TRetType>) state.getCompTracker().alloc(type);
    }
}
