package com.dcrux.buran.scripting.functionsImpl.string;

import com.dcrux.buran.scripting.functions.string.FunStrHash;
import com.dcrux.buran.scripting.iface.IDataState;
import com.dcrux.buran.scripting.iface.IEvaluable;
import com.dcrux.buran.scripting.iface.IFunctionImplFactory;
import com.dcrux.buran.scripting.iface.IFunctionImplementation;
import com.dcrux.buran.scripting.iface.types.BinType;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Buran.
 *
 * @author: ${USER} Date: 04.07.13 Time: 18:31
 */
public class FunStrHashImpl implements IFunctionImplementation<byte[], BinType, FunStrHash> {

    private final FunStrHash.Length length;

    public static IFunctionImplFactory<byte[], BinType, FunStrHash> FACTORY =
            new IFunctionImplFactory<byte[], BinType, FunStrHash>() {

                @Override
                public IFunctionImplementation<byte[], BinType, FunStrHash> getImplementation(
                        FunStrHash function) {
                    return new FunStrHashImpl(function.getLength());
                }

                @Override
                public Class<FunStrHash> getFuncDeclClass() {
                    return FunStrHash.class;
                }
            };

    public FunStrHashImpl(FunStrHash.Length length) {
        this.length = length;
    }

    public static int hashCode32(byte a[]) {
        if (a == null) return 0;

        int result = 1;
        for (byte element : a) {
            result = 31 * result + element;
        }

        return result;
    }

    public static long hashCode64(byte a[]) {
        if (a == null) return 0;

        long result = 1;
        for (byte element : a) {
            result = 31 * result + element;
        }

        return result;
    }

    @Override
    public Object run(IDataState state, IEvaluable... input) {
        final String str = (String) input[0].eval(state);

        final FunStrHash.Length length = this.length;
        try {
            final byte[] bytes = str.getBytes("UTF-8");
            switch (length) {
                case bits32:
                    final ByteBuffer byteBuf32 = ByteBuffer.allocate(length.getNumberOfBytes());
                    byteBuf32.order(ByteOrder.BIG_ENDIAN);
                    byteBuf32.putInt(hashCode32(bytes));
                    return byteBuf32.array();
                case bits64:
                    final ByteBuffer byteBuf64 = ByteBuffer.allocate(length.getNumberOfBytes());
                    byteBuf64.order(ByteOrder.BIG_ENDIAN);
                    byteBuf64.putLong(hashCode64(bytes));
                    return byteBuf64.array();
                default:
                    throw new IllegalArgumentException("Unkown length");
            }
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e);
        }
    }
}
