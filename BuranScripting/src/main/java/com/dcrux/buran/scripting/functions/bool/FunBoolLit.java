package com.dcrux.buran.scripting.functions.bool;

import com.dcrux.buran.scripting.functions.FunctionDeclaration;
import com.dcrux.buran.scripting.iface.AllocType;
import com.dcrux.buran.scripting.iface.CpuComplexityOverflow;
import com.dcrux.buran.scripting.iface.ITypeState;
import com.dcrux.buran.scripting.iface.types.BoolType;

/**
 * Buran.
 *
 * @author: ${USER} Date: 05.07.13 Time: 09:15
 */
public class FunBoolLit extends FunctionDeclaration<BoolType> {

    private final boolean value;

    public static FunBoolLit c(boolean value) {
        return new FunBoolLit(value);
    }

    public FunBoolLit(boolean value) {
        this.value = value;
    }

    public boolean isValue() {
        return value;
    }

    @Override
    public AllocType<BoolType> getMeta(ITypeState state) throws CpuComplexityOverflow {
        final BoolType.BoolRange range;
        if (this.isValue()) {
            range = BoolType.BoolRange.trueOnly;
        } else {
            range = BoolType.BoolRange.falseOnly;
        }
        state.getCompTracker().calc(1);
        final BoolType type = new BoolType(range);
        return state.getCompTracker().allocLiteral(type);
    }
}