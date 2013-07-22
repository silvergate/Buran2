package com.dcrux.buran.scripting.compiler;

import com.dcrux.buran.scripting.iface.IFunctionDeclaration;
import com.dcrux.buran.scripting.iface.IFunctionImplFactory;
import com.dcrux.buran.scripting.iface.IType;

import java.util.HashMap;
import java.util.Map;

/**
 * Buran.
 *
 * @author: ${USER} Date: 05.07.13 Time: 12:39
 */
public class ImplProviderRegistry implements IImplementationProvider {

    private final Map<Class, IFunctionImplFactory> factoryMap =
            new HashMap<Class, IFunctionImplFactory>();

    public void register(IFunctionImplFactory factory) {
        this.factoryMap.put(factory.getFuncDeclClass(), factory);
    }

    @Override
    public <TRetJavaType, TRetType extends IType<TRetJavaType>,
            TFun extends IFunctionDeclaration<TRetType>> IFunctionImplFactory<TRetJavaType,
            TRetType, TFun> getFactory(
            Class<TFun> funDeclClass) {
        return this.factoryMap.get(funDeclClass);
    }
}
