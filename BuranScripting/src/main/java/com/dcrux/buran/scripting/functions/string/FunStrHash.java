package com.dcrux.buran.scripting.functions.string;

import com.dcrux.buran.scripting.functions.FunctionDeclaration;
import com.dcrux.buran.scripting.iface.AllocType;
import com.dcrux.buran.scripting.iface.IFunctionDeclaration;
import com.dcrux.buran.scripting.iface.ITypeState;
import com.dcrux.buran.scripting.iface.ProgrammErrorException;
import com.dcrux.buran.scripting.iface.types.BinType;
import com.dcrux.buran.scripting.iface.types.StringType;

/**
 * Buran.
 *
 * @author: ${USER} Date: 05.07.13 Time: 11:47
 */
public class FunStrHash extends FunctionDeclaration<BinType> {

    public static enum Length {
        bits32(4),
        bits64(8);
        final int numberOfBytes;

        private Length(int numberOfBytes) {
            this.numberOfBytes = numberOfBytes;
        }

        public int getNumberOfBytes() {
            return numberOfBytes;
        }
    }

    private Length length;

    public Length getLength() {
        return length;
    }

    public FunStrHash() {
    }

    public static FunStrHash c(IFunctionDeclaration<StringType> str, Length length) {
        return new FunStrHash(str, length);
    }

    public FunStrHash(IFunctionDeclaration<StringType> str, Length length) {
        addInput(str);
        this.length = length;
    }

    @Override
    public AllocType<BinType> getMeta(ITypeState state) throws ProgrammErrorException {
        final AllocType<StringType> meta1 = getInput(0, StringType.class).getMeta(state);
        final StringType stringType = meta1.getType();

        state.getCompTracker().calc(stringType.getMaxLen());

        final int numOfBytes = this.length.getNumberOfBytes();
        final AllocType<BinType> allocTypeRet =
                state.getCompTracker().alloc(new BinType(numOfBytes, numOfBytes));
        state.getCompTracker().free(meta1);

        return allocTypeRet;
    }
}
