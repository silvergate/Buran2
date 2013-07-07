package com.dcrux.buran.scripting.functionsImpl.integer;

import com.dcrux.buran.scripting.functions.integer.FunIntOp;
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
public class FunIntOpImpl implements IFunctionImplementation<Number, IntegerType, FunIntOp> {

    private final FunIntOp.OpType op;

    public static IFunctionImplFactory<Number, IntegerType, FunIntOp> FACTORY =
            new IFunctionImplFactory<Number, IntegerType, FunIntOp>() {
                @Override
                public IFunctionImplementation<Number, IntegerType, FunIntOp> getImplementation(
                        FunIntOp function) {
                    return new FunIntOpImpl(function.getOp());
                }

                @Override
                public Class<FunIntOp> getFuncDeclClass() {
                    return FunIntOp.class;
                }
            };

    public FunIntOpImpl(FunIntOp.OpType op) {
        this.op = op;
    }

    @Override
    public Object run(IDataState state, IEvaluable... input) {
        final Number v1n = (Number) input[0].eval(state);
        final Number v2n = (Number) input[1].eval(state);
        final long v1 = v1n.longValue();
        final long v2 = v2n.longValue();
        switch (this.op) {
            case add:
                return v1 + v2;
            case sub:
                return v1 - v2;
            case mul:
                return v1 * v2;
            case div:
                return v1 / v2;
            case mod:
                return v1 % v2;
            default:
                throw new IllegalArgumentException("Unknown operatin");
        }
    }
}
