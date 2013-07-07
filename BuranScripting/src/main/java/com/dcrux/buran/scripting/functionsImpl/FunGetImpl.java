package com.dcrux.buran.scripting.functionsImpl;

import com.dcrux.buran.scripting.functions.FunGet;
import com.dcrux.buran.scripting.iface.*;

/**
 * Buran.
 *
 * @author: ${USER} Date: 04.07.13 Time: 18:31
 */
public class FunGetImpl<TJavaRet, TRetType extends IType<TJavaRet>>
        implements IFunctionImplementation<TJavaRet, TRetType, FunGet<TRetType>> {

    private final VarName varName;

    public static <TJavaRetLocal, TRetTypeLocal extends IType<TJavaRetLocal>>
    IFunctionImplFactory<TJavaRetLocal, TRetTypeLocal, FunGet<TRetTypeLocal>> FACTORY() {
        return new IFunctionImplFactory<TJavaRetLocal, TRetTypeLocal, FunGet<TRetTypeLocal>>() {
            @Override
            public IFunctionImplementation<TJavaRetLocal, TRetTypeLocal,
                    FunGet<TRetTypeLocal>> getImplementation(
                    FunGet<TRetTypeLocal> function) {
                return new FunGetImpl<>(function.getVarName());
            }

            @Override
            public Class<?> getFuncDeclClass() {
                return FunGet.class;
            }
        };
    }

    public FunGetImpl(VarName varName) {
        this.varName = varName;
    }

    @Override
    public Object run(IDataState state, IEvaluable... input) {
        return state.getValue(this.varName);
    }
}
