package com.dcrux.buran.scripting.functionsImpl;

import com.dcrux.buran.scripting.functions.FunRet;
import com.dcrux.buran.scripting.iface.*;
import com.dcrux.buran.scripting.iface.types.VoidType;

/**
 * Buran.
 *
 * @author: ${USER} Date: 04.07.13 Time: 18:31
 */
public class FunRetImpl<TInputType extends IType>
        implements IFunctionImplementation<Object, VoidType, FunRet<TInputType>> {

    public static <TInputTypeLocal extends IType> IFunctionImplFactory<Object, VoidType,
            FunRet<TInputTypeLocal>> FACTORY() {
        return new IFunctionImplFactory<Object, VoidType, FunRet<TInputTypeLocal>>() {
            @Override
            public IFunctionImplementation<Object, VoidType, FunRet<TInputTypeLocal>>
            getImplementation(
                    FunRet<TInputTypeLocal> function) {
                return new FunRetImpl<TInputTypeLocal>();
            }

            @Override
            public Class<?> getFuncDeclClass() {
                return FunRet.class;
            }
        };
    }

    public FunRetImpl() {
    }

    @Override
    public Object run(IDataState state, IEvaluable... input) {
        state.ret(input[0].eval(state));
        return null;
    }
}
