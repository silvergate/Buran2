package com.dcrux.buran.scripting.compiler;

import com.dcrux.buran.scripting.iface.IFunctionDeclaration;
import com.dcrux.buran.scripting.iface.IFunctionImplFactory;
import com.dcrux.buran.scripting.iface.IType;

/**
 * Buran.
 *
 * @author: ${USER} Date: 05.07.13 Time: 12:06
 */
public interface IImplementationProvider {
    <TRetJavaType, TRetType extends IType<TRetJavaType>,
            TFun extends IFunctionDeclaration<TRetType>> IFunctionImplFactory<TRetJavaType,
            TRetType, TFun> getFactory(
            Class<TFun> funDeclClass);
}
