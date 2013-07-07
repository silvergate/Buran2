package com.dcrux.buran.scripting.functions.integer;

import com.dcrux.buran.scripting.functions.FunctionDeclaration;
import com.dcrux.buran.scripting.iface.AllocType;
import com.dcrux.buran.scripting.iface.CpuComplexityOverflow;
import com.dcrux.buran.scripting.iface.ITypeState;
import com.dcrux.buran.scripting.iface.types.IntegerType;

/**
 * Buran.
 *
 * @author: ${USER} Date: 04.07.13 Time: 17:40
 */
public class FunIntLit extends FunctionDeclaration<IntegerType> {

    private final Number number;

    public static FunIntLit c(Number number) {
        return new FunIntLit(number);
    }

    public FunIntLit(Number number) {
        this.number = number;
    }

    public Number getNumber() {
        return number;
    }

    @Override
    public AllocType<IntegerType> getMeta(ITypeState state) throws CpuComplexityOverflow {
        state.getCompTracker().calc(1);
        return state.getCompTracker()
                .allocLiteral(new IntegerType(this.number.longValue(), this.number.longValue()));
    }
}
