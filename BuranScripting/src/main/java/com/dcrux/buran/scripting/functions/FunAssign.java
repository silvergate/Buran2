package com.dcrux.buran.scripting.functions;

import com.dcrux.buran.scripting.iface.*;
import com.dcrux.buran.scripting.iface.types.VoidType;

/**
 * Buran.
 *
 * @author: ${USER} Date: 04.07.13 Time: 17:40
 */
public class FunAssign<TInputType extends IType> extends FunctionDeclaration<VoidType> {

    private VarName varName;

    private FunAssign() {
    }

    public static <TInputTypeLocal extends IType> FunAssign<TInputTypeLocal> c(VarName varName,
            IFunctionDeclaration<TInputTypeLocal> input) {
        return new FunAssign<TInputTypeLocal>(varName, input);
    }

    public FunAssign(VarName varName, IFunctionDeclaration<TInputType> input) {
        this.varName = varName;
        addInput(input);
    }

    public VarName getVarName() {
        return varName;
    }

    @Override
    public AllocType<VoidType> getMeta(ITypeState state) throws ProgrammErrorException {
        final IFunctionDeclaration<?> decl = getInput().get(0);
        final AllocType<? extends IType> meta = decl.getMeta(state);

        state.setType(this.varName, meta.getType(), false);
        state.getCompTracker().free(meta);

        return state.getCompTracker().allocLiteral(VoidType.SINGLETON);
    }
}
