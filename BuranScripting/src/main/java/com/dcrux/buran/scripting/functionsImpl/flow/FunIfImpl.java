package com.dcrux.buran.scripting.functionsImpl.flow;

import com.dcrux.buran.scripting.functions.flow.FunIf;
import com.dcrux.buran.scripting.iface.*;
import com.dcrux.buran.scripting.iface.types.VoidType;

/**
 * Buran.
 *
 * @author: ${USER} Date: 04.07.13 Time: 18:31
 */
public class FunIfImpl implements IFunctionImplementation<Object, VoidType, FunIf> {

    private final int lineNum;

    public static IFunctionImplFactory<Object, VoidType, FunIf> FACTORY =
            new IFunctionImplFactory<Object, VoidType, FunIf>() {
                @Override
                public IFunctionImplementation<Object, VoidType, FunIf> getImplementation(
                        FunIf function) {
                    return new FunIfImpl(function.getJumpTo().getLineNum());
                }

                @Override
                public Class<FunIf> getFuncDeclClass() {
                    return FunIf.class;
                }
            };

    public FunIfImpl(LineNum lineNum) {
        this.lineNum = lineNum.getNum();
    }

    @Override
    public Object run(IDataState state, IEvaluable... input) {
        final Boolean test = (Boolean) input[0].eval(state);
        if (test) {
            state.jumpTo(this.lineNum);
        }
        return null;
    }
}
