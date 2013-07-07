package com.dcrux.buran.scripting.functionsImpl.integer;

import com.dcrux.buran.scripting.functions.integer.FunIntLit;
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
public class FunIntLitImpl implements IFunctionImplementation<Number, IntegerType, FunIntLit> {

    private final Number value;

    public static IFunctionImplFactory<Number, IntegerType, FunIntLit> FACTORY =
            new IFunctionImplFactory<Number, IntegerType, FunIntLit>() {
                @Override
                public IFunctionImplementation<Number, IntegerType, FunIntLit> getImplementation(
                        FunIntLit function) {
                    return new FunIntLitImpl(function.getNumber());
                }

                @Override
                public Class<FunIntLit> getFuncDeclClass() {
                    return FunIntLit.class;
                }
            };

    public FunIntLitImpl(Number value) {
        this.value = value;
    }

    @Override
    public Object run(IDataState state, IEvaluable... input) {
        return this.value;
    }
}
