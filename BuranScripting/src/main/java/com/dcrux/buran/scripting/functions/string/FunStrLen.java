package com.dcrux.buran.scripting.functions.string;

import com.dcrux.buran.scripting.functions.FunctionDeclaration;
import com.dcrux.buran.scripting.iface.AllocType;
import com.dcrux.buran.scripting.iface.IFunctionDeclaration;
import com.dcrux.buran.scripting.iface.ITypeState;
import com.dcrux.buran.scripting.iface.ProgrammErrorException;
import com.dcrux.buran.scripting.iface.types.IntegerType;
import com.dcrux.buran.scripting.iface.types.StringType;

/**
 * Buran.
 *
 * @author: ${USER} Date: 05.07.13 Time: 11:47
 */
public class FunStrLen extends FunctionDeclaration<IntegerType> {

    private FunStrLen() {
    }

    public static FunStrLen c(IFunctionDeclaration<StringType> input) {
        return new FunStrLen(input);
    }

    public FunStrLen(IFunctionDeclaration<StringType> input) {
        addInput(input);
    }

    @Override
    public AllocType<IntegerType> getMeta(ITypeState state) throws ProgrammErrorException {
        final IFunctionDeclaration<StringType> inputFun = getInput(0, StringType.class);
        final AllocType<StringType> meta = inputFun.getMeta(state);
        final StringType type = meta.getType();

        final AllocType<IntegerType> allocTypeRet =
                state.getCompTracker().alloc(new IntegerType(type.getMinLen(), type.getMaxLen()));
        state.getCompTracker().calc(1);
        state.getCompTracker().free(meta);

        return allocTypeRet;
    }
}
