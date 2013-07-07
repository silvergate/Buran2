package com.dcrux.buran.scripting.functionsImpl;

import com.dcrux.buran.scripting.functions.FunAssign;
import com.dcrux.buran.scripting.iface.*;
import com.dcrux.buran.scripting.iface.types.VoidType;

/**
 * Buran.
 *
 * @author: ${USER} Date: 04.07.13 Time: 18:31
 */
public class FunAssignImpl<TInputType extends IType>
        implements IFunctionImplementation<Object, VoidType, FunAssign<TInputType>> {

    private final VarName varName;

    public static <TInputTypeLocal extends IType> IFunctionImplFactory<Object, VoidType,
            FunAssign<TInputTypeLocal>> FACTORY() {
        return new IFunctionImplFactory<Object, VoidType, FunAssign<TInputTypeLocal>>() {
            @Override
            public IFunctionImplementation<Object, VoidType, FunAssign<TInputTypeLocal>>
            getImplementation(
                    FunAssign<TInputTypeLocal> function) {
                return new FunAssignImpl<TInputTypeLocal>(function.getVarName());
            }

            @Override
            public Class<?> getFuncDeclClass() {
                return FunAssign.class;
            }
        };
    }

    public FunAssignImpl(VarName varName) {
        this.varName = varName;
    }

    @Override
    public Object run(IDataState state, IEvaluable... input) {
        state.setValue(varName, input[0].eval(state));
        return null;
    }
}
