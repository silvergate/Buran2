package com.dcrux.buran.scripting.functionsImpl.integer;

import com.dcrux.buran.scripting.functions.integer.FunIntCmp;
import com.dcrux.buran.scripting.iface.IDataState;
import com.dcrux.buran.scripting.iface.IEvaluable;
import com.dcrux.buran.scripting.iface.IFunctionImplFactory;
import com.dcrux.buran.scripting.iface.IFunctionImplementation;
import com.dcrux.buran.scripting.iface.types.BoolType;

/**
 * Buran.
 *
 * @author: ${USER} Date: 04.07.13 Time: 18:31
 */
public class FunIntCmpImpl implements IFunctionImplementation<Boolean, BoolType, FunIntCmp> {

    private final FunIntCmp.CmpOp op;

    public static IFunctionImplFactory<Boolean, BoolType, FunIntCmp> FACTORY =
            new IFunctionImplFactory<Boolean, BoolType, FunIntCmp>() {
                @Override
                public IFunctionImplementation<Boolean, BoolType, FunIntCmp> getImplementation(
                        FunIntCmp function) {
                    return new FunIntCmpImpl(function.getOp());
                }

                @Override
                public Class<FunIntCmp> getFuncDeclClass() {
                    return FunIntCmp.class;
                }
            };

    public FunIntCmpImpl(FunIntCmp.CmpOp op) {
        this.op = op;
    }

    @Override
    public Object run(IDataState state, IEvaluable... input) {
        final Number v1n = (Number) input[0].eval(state);
        final Number v2n = (Number) input[1].eval(state);
        final long v1 = v1n.longValue();
        final long v2 = v2n.longValue();
        switch (this.op) {
            case greater:
                return v1 > v2;
            case lesser:
                return v1 < v2;
            case equal:
                return v1 == v2;
            default:
                throw new IllegalArgumentException("Unknown cmp operation");
        }
    }
}
