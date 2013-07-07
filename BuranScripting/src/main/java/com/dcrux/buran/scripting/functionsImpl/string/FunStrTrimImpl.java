package com.dcrux.buran.scripting.functionsImpl.string;

import com.dcrux.buran.scripting.functions.string.FunStrTrim;
import com.dcrux.buran.scripting.iface.IDataState;
import com.dcrux.buran.scripting.iface.IEvaluable;
import com.dcrux.buran.scripting.iface.IFunctionImplFactory;
import com.dcrux.buran.scripting.iface.IFunctionImplementation;
import com.dcrux.buran.scripting.iface.types.StringType;

/**
 * Buran.
 *
 * @author: ${USER} Date: 04.07.13 Time: 18:31
 */
public class FunStrTrimImpl implements IFunctionImplementation<String, StringType, FunStrTrim> {

    private final static FunStrTrimImpl SINGLETON = new FunStrTrimImpl();

    public static IFunctionImplFactory<String, StringType, FunStrTrim> FACTORY =
            new IFunctionImplFactory<String, StringType, FunStrTrim>() {

                @Override
                public IFunctionImplementation<String, StringType, FunStrTrim> getImplementation(
                        FunStrTrim function) {
                    return SINGLETON;
                }

                @Override
                public Class<FunStrTrim> getFuncDeclClass() {
                    return FunStrTrim.class;
                }
            };

    @Override
    public Object run(IDataState state, IEvaluable... input) {
        final String str = (String) input[0].eval(state);
        return str.trim();
    }
}
