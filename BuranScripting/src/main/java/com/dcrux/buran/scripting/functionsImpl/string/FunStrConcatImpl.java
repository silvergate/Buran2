package com.dcrux.buran.scripting.functionsImpl.string;

import com.dcrux.buran.scripting.functions.string.FunStrConcat;
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
public class FunStrConcatImpl implements IFunctionImplementation<String, StringType, FunStrConcat> {

    private final static FunStrConcatImpl SINGLETON = new FunStrConcatImpl();

    public static IFunctionImplFactory<String, StringType, FunStrConcat> FACTORY =
            new IFunctionImplFactory<String, StringType, FunStrConcat>() {

                @Override
                public IFunctionImplementation<String, StringType, FunStrConcat> getImplementation(
                        FunStrConcat function) {
                    return SINGLETON;
                }

                @Override
                public Class<FunStrConcat> getFuncDeclClass() {
                    return FunStrConcat.class;
                }
            };

    @Override
    public Object run(IDataState state, IEvaluable... input) {
        final String str1 = (String) input[0].eval(state);
        final String str2 = (String) input[1].eval(state);
        final StringBuffer sb = new StringBuffer();
        sb.append(str1);
        sb.append(str2);

        return sb.toString();
    }
}
