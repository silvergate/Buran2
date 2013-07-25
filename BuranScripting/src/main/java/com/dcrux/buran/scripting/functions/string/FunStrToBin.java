package com.dcrux.buran.scripting.functions.string;

import com.dcrux.buran.scripting.functions.FunctionDeclaration;
import com.dcrux.buran.scripting.iface.AllocType;
import com.dcrux.buran.scripting.iface.IFunctionDeclaration;
import com.dcrux.buran.scripting.iface.ITypeState;
import com.dcrux.buran.scripting.iface.ProgrammErrorException;
import com.dcrux.buran.scripting.iface.types.BinType;
import com.dcrux.buran.scripting.iface.types.IntegerType;
import com.dcrux.buran.scripting.iface.types.StringType;

/**
 * Buran.
 *
 * @author: ${USER} Date: 05.07.13 Time: 11:47
 */
public class FunStrToBin extends FunctionDeclaration<BinType> {

    private IntegerType.NumOfBits numOfBits;

    public FunStrToBin() {
    }

    public static FunStrToBin c(IFunctionDeclaration<StringType> str,
            IntegerType.NumOfBits numOfBits) {
        return new FunStrToBin(str, numOfBits);
    }

    public FunStrToBin(IFunctionDeclaration<StringType> str, IntegerType.NumOfBits numOfBits) {
        addInput(str);
    }

    public IntegerType.NumOfBits getNumOfBits() {
        return numOfBits;
    }

    @Override
    public AllocType<BinType> getMeta(ITypeState state) throws ProgrammErrorException {
        final AllocType<StringType> meta1 = getInput(0, StringType.class).getMeta(state);

        state.getCompTracker().calc(this.numOfBits.getBytes());

        final AllocType<BinType> allocTypeRet = state.getCompTracker()
                .alloc(new BinType(this.numOfBits.getBytes(), this.numOfBits.getBytes()));
        state.getCompTracker().free(meta1);

        return allocTypeRet;
    }
}
