package com.dcrux.buran.scripting.functions;

import com.dcrux.buran.scripting.iface.IFunctionDeclaration;
import com.dcrux.buran.scripting.iface.IType;

import java.util.ArrayList;
import java.util.List;

/**
 * Buran.
 *
 * @author: ${USER} Date: 04.07.13 Time: 17:41
 */
public abstract class FunctionDeclaration<TRetType extends IType>
        implements IFunctionDeclaration<TRetType> {

    private final List<IFunctionDeclaration<?>> input = new ArrayList<IFunctionDeclaration<?>>();

    protected void addInput(IFunctionDeclaration<?> function) {
        this.input.add(function);
    }

    protected <TType extends IType> IFunctionDeclaration<TType> getInput(int index,
            Class<TType> type) {
        return (IFunctionDeclaration<TType>) this.input.get(index);
    }

    @Override
    public IFunctionDeclaration<?> getInput(int index) {
        return this.input.get(index);
    }

    @Override
    public List<IFunctionDeclaration<?>> getInput() {
        return this.input;
    }
}
