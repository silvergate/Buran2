package com.dcrux.buran.scripting.functionsImpl.string;

import com.dcrux.buran.scripting.functions.string.FunStrLen;
import com.dcrux.buran.scripting.iface.IDataState;
import com.dcrux.buran.scripting.iface.IEvaluable;
import com.dcrux.buran.scripting.iface.IFunctionImplFactory;
import com.dcrux.buran.scripting.iface.IFunctionImplementation;
import com.dcrux.buran.scripting.iface.types.IntegerType;

/**
 * Buran.
 *
 * @author: ${USER} Date: 04.07.13 Time: 18:31
 */
public class FunStrLenImpl implements IFunctionImplementation<Number, IntegerType, FunStrLen> {

    public static IFunctionImplFactory<Number, IntegerType, FunStrLen> FACTORY =
            new IFunctionImplFactory<Number, IntegerType, FunStrLen>() {

                @Override
                public IFunctionImplementation<Number, IntegerType, FunStrLen> getImplementation(
                        FunStrLen function) {
                    return new FunStrLenImpl();
                }

                @Override
                public Class<FunStrLen> getFuncDeclClass() {
                    return FunStrLen.class;
                }
            };

    @Override
    public Object run(IDataState state, IEvaluable... input) {
        final String str = (String) input[0].eval(state);
        return str.length();
    }
}
