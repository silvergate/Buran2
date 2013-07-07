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
public class FunStrConcat extends FunctionDeclaration<StringType> {

    public static FunStrConcat c(IFunctionDeclaration<StringType> str1,
            IFunctionDeclaration<StringType> str2) {
        return new FunStrConcat(str1, str2);
    }

    public FunStrConcat(IFunctionDeclaration<StringType> str1,
            IFunctionDeclaration<StringType> str2) {
        addInput(str1);
        addInput(str2);
    }

    @Override
    public AllocType<StringType> getMeta(ITypeState state) throws ProgrammErrorException {
        final AllocType<StringType> meta1 = getInput(0, StringType.class).getMeta(state);
        final AllocType<StringType> meta2 = getInput(1, StringType.class).getMeta(state);

        final long minLen = (long) meta1.getType().getMinLen() + (long) meta2.getType().getMinLen();
        final long maxLen = (long) meta1.getType().getMaxLen() + (long) meta2.getType().getMaxLen();

        if (minLen > Integer.MAX_VALUE) {
            throw new ProgrammErrorException("Concat string length (min) may exceed int32 limit.");
        }
        if (maxLen > Integer.MAX_VALUE) {
            throw new ProgrammErrorException("Concat string length (max) may exceed int32 limit.");
        }

        final AllocType<StringType> allocTypeRet =
                state.getCompTracker().alloc(new StringType((int) minLen, (int) maxLen));
        state.getCompTracker().calc((int) maxLen);
        state.getCompTracker().free(meta1);
        state.getCompTracker().free(meta2);

        return allocTypeRet;
    }
}
