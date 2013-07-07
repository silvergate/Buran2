package com.dcrux.buran.scripting.iface;

import com.sun.istack.internal.Nullable;

/**
 * Buran.
 *
 * @author: ${USER} Date: 04.07.13 Time: 17:57
 */
public interface IFunctionImplFactory<TRetJavaType, TRetType extends IType<TRetJavaType>,
        TFun extends IFunctionDeclaration<TRetType>> {

    @Nullable
    IFunctionImplementation<TRetJavaType, TRetType, TFun> getImplementation(TFun function);

    Class<?> getFuncDeclClass();
}
