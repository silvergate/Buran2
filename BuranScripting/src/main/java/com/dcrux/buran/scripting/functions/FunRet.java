package com.dcrux.buran.scripting.functions;

import com.dcrux.buran.scripting.iface.*;
import com.dcrux.buran.scripting.iface.types.VoidType;

/**
 * Buran.
 *
 * @author: ${USER} Date: 04.07.13 Time: 17:40
 */
public class FunRet<TInputType extends IType> extends FunctionDeclaration<VoidType> {

    private FunRet() {
    }

    public static <TInputTypeLocal extends IType> FunRet<TInputTypeLocal> c(
            IFunctionDeclaration<TInputTypeLocal> input) {
        return new FunRet<TInputTypeLocal>(input);
    }

    public FunRet(IFunctionDeclaration<TInputType> input) {
        addInput(input);
    }

    @Override
    public AllocType<VoidType> getMeta(ITypeState state) throws ProgrammErrorException {
        final IFunctionDeclaration<?> decl = getInput().get(0);
        final AllocType<? extends IType> meta = decl.getMeta(state);
        state.ret(meta.getType());
        state.getCompTracker().free(meta);
        return state.getCompTracker().allocLiteral(VoidType.SINGLETON);
    }
}
