package com.dcrux.buran.scripting.functionsImpl.integer;

import com.dcrux.buran.scripting.functions.integer.FunIntToBin;
import com.dcrux.buran.scripting.iface.IDataState;
import com.dcrux.buran.scripting.iface.IEvaluable;
import com.dcrux.buran.scripting.iface.IFunctionImplFactory;
import com.dcrux.buran.scripting.iface.IFunctionImplementation;
import com.dcrux.buran.scripting.iface.types.BinType;
import com.dcrux.buran.scripting.iface.types.IntegerType;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Buran.
 *
 * @author: ${USER} Date: 04.07.13 Time: 18:31
 */
public class FunIntToBinImpl implements IFunctionImplementation<byte[], BinType, FunIntToBin> {

    private final IntegerType.NumOfBits numOfBits;

    public static IFunctionImplFactory<byte[], BinType, FunIntToBin> FACTORY =
            new IFunctionImplFactory<byte[], BinType, FunIntToBin>() {
                @Override
                public IFunctionImplementation<byte[], BinType, FunIntToBin> getImplementation(
                        FunIntToBin function) {
                    return new FunIntToBinImpl(function.getNumOfBits());
                }

                @Override
                public Class<FunIntToBin> getFuncDeclClass() {
                    return FunIntToBin.class;
                }
            };

    public FunIntToBinImpl(IntegerType.NumOfBits numOfBits) {
        this.numOfBits = numOfBits;
    }

    @Override
    public Object run(IDataState state, IEvaluable... input) {
        final Number number = (Number) input[0].eval(state);
        final ByteBuffer byteBuf = ByteBuffer.allocate(numOfBits.getBytes());
        byteBuf.order(ByteOrder.BIG_ENDIAN);
        switch (numOfBits) {
            case int8:
                byteBuf.put(number.byteValue());
                break;
            case int16:
                byteBuf.putShort(number.shortValue());
                break;
            case int32:
                byteBuf.putInt(number.intValue());
                break;
            case int64:
                byteBuf.putLong(number.longValue());
                break;
            default:
                throw new IllegalArgumentException("Unknown number of bits");
        }
        return byteBuf.array();
    }
}
