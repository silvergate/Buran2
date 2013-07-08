package com.dcrux.buran.scripting.iface;

import java.io.Serializable;

/**
 * Buran.
 *
 * @author: ${USER} Date: 04.07.13 Time: 17:34
 */
public interface IFunctionImplementation<TRetJavaType, TRetType extends IType<TRetJavaType>,
        TFun extends IFunctionDeclaration<TRetType>>
        extends Serializable {
    Object run(IDataState state, IEvaluable... input);
}
