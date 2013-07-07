package com.dcrux.buran.scripting.functions.string;

import com.dcrux.buran.scripting.functions.FunctionDeclaration;
import com.dcrux.buran.scripting.iface.AllocType;
import com.dcrux.buran.scripting.iface.IFunctionDeclaration;
import com.dcrux.buran.scripting.iface.ITypeState;
import com.dcrux.buran.scripting.iface.ProgrammErrorException;
import com.dcrux.buran.scripting.iface.types.StringType;

/**
 * Buran.
 *
 * @author: ${USER} Date: 05.07.13 Time: 11:47
 */
public class FunStrTrim extends FunctionDeclaration<StringType> {

    public static FunStrTrim c(IFunctionDeclaration<StringType> input) {
        return new FunStrTrim(input);
    }

    public FunStrTrim(IFunctionDeclaration<StringType> input) {
        addInput(input);
    }

    @Override
    public AllocType<StringType> getMeta(ITypeState state) throws ProgrammErrorException {
        final IFunctionDeclaration<StringType> inputFun = getInput(0, StringType.class);
        final AllocType<StringType> meta = inputFun.getMeta(state);
        final StringType type = meta.getType();

        final AllocType<StringType> allocTypeRet =
                state.getCompTracker().alloc(new StringType(0, type.getMaxLen()));
        state.getCompTracker().calc(type.getMaxLen());
        state.getCompTracker().free(meta);

        return allocTypeRet;
    }
}
