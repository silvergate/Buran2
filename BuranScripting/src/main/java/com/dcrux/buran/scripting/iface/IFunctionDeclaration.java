package com.dcrux.buran.scripting.iface;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;
import java.util.List;

/**
 * Buran.
 *
 * @author: ${USER} Date: 04.07.13 Time: 17:28
 */
public interface IFunctionDeclaration<TRetType extends IType> extends Serializable, IsSerializable {
    List<IFunctionDeclaration<?>> getInput();

    IFunctionDeclaration<?> getInput(int index);

    AllocType<TRetType> getMeta(ITypeState state) throws ProgrammErrorException;
}
