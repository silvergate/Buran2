package com.dcrux.buran.scripting.functions.bin;

import com.dcrux.buran.scripting.functions.FunctionDeclaration;
import com.dcrux.buran.scripting.iface.AllocType;
import com.dcrux.buran.scripting.iface.IFunctionDeclaration;
import com.dcrux.buran.scripting.iface.ITypeState;
import com.dcrux.buran.scripting.iface.ProgrammErrorException;
import com.dcrux.buran.scripting.iface.types.BinType;

import java.util.ArrayList;
import java.util.List;

/**
 * Buran.
 *
 * @author: ${USER} Date: 05.07.13 Time: 11:47
 */
public class FunBinConcat extends FunctionDeclaration<BinType> {

    public FunBinConcat() {
    }

    public static FunBinConcat c(IFunctionDeclaration<BinType>... bins) {
        return new FunBinConcat(bins);
    }

    public FunBinConcat(IFunctionDeclaration<BinType>... bins) {
        for (final IFunctionDeclaration<BinType> bin : bins) {
            addInput(bin);
        }
    }

    @Override
    public AllocType<BinType> getMeta(ITypeState state) throws ProgrammErrorException {
        int numOfInputs = getNumberOfInputs();
        final List<AllocType<BinType>> inputs = new ArrayList<AllocType<BinType>>();
        long maxNumOfBytes = 0;
        long minNumOfBytes = 0;
        for (int i = 0; i < numOfInputs; i++) {
            final AllocType<BinType> meta = getInput(i, BinType.class).getMeta(state);
            maxNumOfBytes += meta.getType().getMaxBytes();
            minNumOfBytes += meta.getType().getMinBytes();
            inputs.add(meta);
        }

        if ((maxNumOfBytes > Integer.MAX_VALUE) || (minNumOfBytes > Integer.MAX_VALUE)) {
            throw new ProgrammErrorException("Binary length overflow.");
        }

        state.getCompTracker().calc((int) maxNumOfBytes);

        final AllocType<BinType> allocTypeRet =
                state.getCompTracker().alloc(new BinType((int) minNumOfBytes, (int) maxNumOfBytes));
        for (final AllocType<BinType> meta : inputs) {
            state.getCompTracker().free(meta);
        }

        return allocTypeRet;
    }
}
