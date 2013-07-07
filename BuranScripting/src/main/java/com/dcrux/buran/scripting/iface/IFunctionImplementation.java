package com.dcrux.buran.scripting.iface;

/**
 * Buran.
 *
 * @author: ${USER} Date: 04.07.13 Time: 17:34
 */
public interface IFunctionImplementation<TRetJavaType, TRetType extends IType<TRetJavaType>,
        TFun extends IFunctionDeclaration<TRetType>> {
    Object run(IDataState state, IEvaluable... input);
}
