package com.dcrux.buran.scripting.functionsImpl.string;

import com.dcrux.buran.scripting.functions.string.FunStrToBin;
import com.dcrux.buran.scripting.iface.IDataState;
import com.dcrux.buran.scripting.iface.IEvaluable;
import com.dcrux.buran.scripting.iface.IFunctionImplFactory;
import com.dcrux.buran.scripting.iface.IFunctionImplementation;
import com.dcrux.buran.scripting.iface.types.BinType;
import com.dcrux.buran.scripting.iface.types.IntegerType;

import java.io.UnsupportedEncodingException;

/**
 * Buran.
 *
 * @author: ${USER} Date: 04.07.13 Time: 18:31
 */
public class FunStrToBinImpl implements IFunctionImplementation<byte[], BinType, FunStrToBin> {

    private final IntegerType.NumOfBits numOfBits;

    public static IFunctionImplFactory<byte[], BinType, FunStrToBin> FACTORY =
            new IFunctionImplFactory<byte[], BinType, FunStrToBin>() {

                @Override
                public IFunctionImplementation<byte[], BinType, FunStrToBin> getImplementation(
                        FunStrToBin function) {
                    return new FunStrToBinImpl(function.getNumOfBits());
                }

                @Override
                public Class<FunStrToBin> getFuncDeclClass() {
                    return FunStrToBin.class;
                }
            };

    public FunStrToBinImpl(IntegerType.NumOfBits numOfBits) {
        this.numOfBits = numOfBits;
    }

    @Override
    public Object run(IDataState state, IEvaluable... input) {
        final String str = (String) input[0].eval(state);
        final int reqNumOfBytes = this.numOfBits.getBytes();
        try {
            final String limitedString;
            final int requiredChars = reqNumOfBytes;
            if (str.length() > requiredChars) {
                limitedString = str.substring(0, requiredChars - 1);
            } else {
                limitedString = str;
            }

            final byte[] bytes = limitedString.getBytes("UTF-8");
            if (bytes.length == reqNumOfBytes) {
                return bytes;
            } else {
                /* If not latin, might be more than number of given bytes / or less */
                final byte[] resizedBytes = new byte[reqNumOfBytes];
                System.arraycopy(bytes, 0, resizedBytes, 0, bytes.length);
                return resizedBytes;
            }
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e);
        }
    }
}
