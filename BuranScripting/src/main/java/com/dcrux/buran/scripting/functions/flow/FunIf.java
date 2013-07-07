package com.dcrux.buran.scripting.functions.flow;

import com.dcrux.buran.scripting.functions.FunctionDeclaration;
import com.dcrux.buran.scripting.iface.*;
import com.dcrux.buran.scripting.iface.types.BoolType;
import com.dcrux.buran.scripting.iface.types.VoidType;

/**
 * Buran.
 *
 * @author: ${USER} Date: 05.07.13 Time: 09:03
 */
public class FunIf extends FunctionDeclaration<VoidType> {

    private final ILineNumProvider jumpTo;

    public static FunIf c(ILineNumProvider jumpTo, IFunctionDeclaration<BoolType> test) {
        return new FunIf(jumpTo, test);
    }

    public FunIf(ILineNumProvider jumpTo, IFunctionDeclaration<BoolType> test) {
        this.jumpTo = jumpTo;
        addInput(test);
    }

    public ILineNumProvider getJumpTo() {
        return jumpTo;
    }

    @Override
    public AllocType<VoidType> getMeta(ITypeState state) throws ProgrammErrorException {
        IFunctionDeclaration<BoolType> boolTypeFun = (IFunctionDeclaration<BoolType>) getInput(0);
        final AllocType<BoolType> boolTypeAlloc = boolTypeFun.getMeta(state);
        final BoolType boolType = boolTypeAlloc.getType();
        if (boolType.getRange() == BoolType.BoolRange.trueOnly) {
            state.jumpTo(this.jumpTo);
        }
        if (boolType.getRange() == BoolType.BoolRange.trueOrFalse) {
            state.branch(this.jumpTo);
        }
        state.getCompTracker().calc(1);

        state.getCompTracker().free(boolTypeAlloc);
        return state.getCompTracker().allocLiteral(VoidType.SINGLETON);
    }
}
