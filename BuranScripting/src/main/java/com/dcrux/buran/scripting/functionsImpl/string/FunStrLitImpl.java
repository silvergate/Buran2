package com.dcrux.buran.scripting.functionsImpl.string;

import com.dcrux.buran.scripting.functions.string.FunStrLit;
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
public class FunStrLitImpl implements IFunctionImplementation<String, StringType, FunStrLit> {

    private final String value;

    public static IFunctionImplFactory<String, StringType, FunStrLit> FACTORY =
            new IFunctionImplFactory<String, StringType, FunStrLit>() {

                @Override
                public IFunctionImplementation<String, StringType, FunStrLit> getImplementation(
                        FunStrLit function) {
                    return new FunStrLitImpl(function.getValue());
                }

                @Override
                public Class<FunStrLit> getFuncDeclClass() {
                    return FunStrLit.class;
                }
            };

    public String getValue() {
        return value;
    }

    public FunStrLitImpl(String value) {
        this.value = value;
    }

    @Override
    public Object run(IDataState state, IEvaluable... input) {
        return this.value;
    }
}
