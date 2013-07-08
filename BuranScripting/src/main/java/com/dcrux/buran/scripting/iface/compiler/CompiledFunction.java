package com.dcrux.buran.scripting.iface.compiler;

import com.dcrux.buran.scripting.iface.*;

import java.io.Serializable;

/**
 * Buran.
 *
 * @author: ${USER} Date: 05.07.13 Time: 12:13
 */
public class CompiledFunction<TRetJavaType, TRetType extends IType<TRetJavaType>,
        TFun extends IFunctionDeclaration<TRetType>>
        implements IEvaluable, Serializable {

    private final IFunctionImplementation<TRetJavaType, TRetType, TFun> implementation;
    private final IEvaluable[] args;

    public CompiledFunction(IFunctionImplementation<TRetJavaType, TRetType, TFun> implementation,
            IEvaluable[] args) {
        this.implementation = implementation;
        this.args = args;
    }

    public TRetJavaType run(IDataState state) {
        return (TRetJavaType) this.implementation.run(state, args);
    }

    @Override
    public Object eval(IDataState state) {
        return run(state);
    }
}
