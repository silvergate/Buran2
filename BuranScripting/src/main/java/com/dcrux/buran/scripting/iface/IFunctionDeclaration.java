package com.dcrux.buran.scripting.iface;

import java.util.List;

/**
 * Buran.
 *
 * @author: ${USER} Date: 04.07.13 Time: 17:28
 */
public interface IFunctionDeclaration<TRetType extends IType> {
    List<IFunctionDeclaration<?>> getInput();

    IFunctionDeclaration<?> getInput(int index);

    AllocType<TRetType> getMeta(ITypeState state) throws ProgrammErrorException;
}
