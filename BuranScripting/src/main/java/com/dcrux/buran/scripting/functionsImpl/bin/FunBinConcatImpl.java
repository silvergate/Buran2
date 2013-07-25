package com.dcrux.buran.scripting.functionsImpl.bin;

import com.dcrux.buran.scripting.functions.bin.FunBinConcat;
import com.dcrux.buran.scripting.iface.IDataState;
import com.dcrux.buran.scripting.iface.IEvaluable;
import com.dcrux.buran.scripting.iface.IFunctionImplFactory;
import com.dcrux.buran.scripting.iface.IFunctionImplementation;
import com.dcrux.buran.scripting.iface.types.BinType;

import java.util.ArrayList;
import java.util.List;

/**
 * Buran.
 *
 * @author: ${USER} Date: 04.07.13 Time: 18:31
 */
public class FunBinConcatImpl implements IFunctionImplementation<byte[], BinType, FunBinConcat> {

    public static IFunctionImplFactory<byte[], BinType, FunBinConcat> FACTORY =
            new IFunctionImplFactory<byte[], BinType, FunBinConcat>() {

                @Override
                public IFunctionImplementation<byte[], BinType, FunBinConcat> getImplementation(
                        FunBinConcat function) {
                    return new FunBinConcatImpl();
                }

                @Override
                public Class<FunBinConcat> getFuncDeclClass() {
                    return FunBinConcat.class;
                }
            };

    @Override
    public Object run(IDataState state, IEvaluable... input) {
        final List<byte[]> evaluated = new ArrayList<byte[]>();
        int len = 0;
        for (final IEvaluable evaluable : input) {
            byte[] data = (byte[]) evaluable.eval(state);
            evaluated.add(data);
            len += data.length;
        }

        final byte[] result = new byte[len];
        int pos = 0;
        for (final byte[] element : evaluated) {
            System.arraycopy(element, 0, result, pos, element.length);
            pos += element.length;
        }

        return result;
    }
}
