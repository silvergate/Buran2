package com.dcrux.buran.scripting.functions.integer;

import com.dcrux.buran.scripting.functions.FunctionDeclaration;
import com.dcrux.buran.scripting.iface.AllocType;
import com.dcrux.buran.scripting.iface.IFunctionDeclaration;
import com.dcrux.buran.scripting.iface.ITypeState;
import com.dcrux.buran.scripting.iface.ProgrammErrorException;
import com.dcrux.buran.scripting.iface.types.BinType;
import com.dcrux.buran.scripting.iface.types.IntegerType;

import java.text.MessageFormat;

/**
 * Buran.
 *
 * @author: ${USER} Date: 04.07.13 Time: 17:40
 */
public class FunIntToBin extends FunctionDeclaration<BinType> {

    private final IntegerType.NumOfBits numOfBits;

    public static FunIntToBin c(IFunctionDeclaration<IntegerType> i1,
            IntegerType.NumOfBits numOfBits) {
        return new FunIntToBin(i1, numOfBits);
    }

    public FunIntToBin(IFunctionDeclaration<IntegerType> i1, IntegerType.NumOfBits numOfBits) {
        this.numOfBits = numOfBits;
        addInput(i1);
    }

    public IntegerType.NumOfBits getNumOfBits() {
        return numOfBits;
    }

    @Override
    public AllocType<BinType> getMeta(ITypeState state) throws ProgrammErrorException {
        final AllocType<IntegerType> iF1 = getInput(0, IntegerType.class).getMeta(state);
        final IntegerType it1 = iF1.getType();

        state.getCompTracker()
                .calc(Math.max(this.numOfBits.getBytes(), it1.getRequiredBits().getBytes()));

        if (this.numOfBits.getBytes() < it1.getRequiredBits().getBytes()) {
            throw new ProgrammErrorException(MessageFormat.format("Cannot convert integer " +
                    "to binary using {0} bytes. The given integer requires at least {1} " +
                    "bytes.", this.numOfBits.getBytes(), it1.getRequiredBits().getBytes()));
        }

        final AllocType<BinType> allocTypeRet = state.getCompTracker()
                .alloc(new BinType(this.numOfBits.getBytes(), this.numOfBits.getBytes()));
        state.getCompTracker().free(iF1);

        return allocTypeRet;
    }
}
