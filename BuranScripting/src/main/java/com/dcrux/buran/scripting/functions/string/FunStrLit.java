package com.dcrux.buran.scripting.functions.string;

import com.dcrux.buran.scripting.functions.FunctionDeclaration;
import com.dcrux.buran.scripting.iface.AllocType;
import com.dcrux.buran.scripting.iface.CpuComplexityOverflow;
import com.dcrux.buran.scripting.iface.ITypeState;
import com.dcrux.buran.scripting.iface.types.StringType;

/**
 * Buran.
 *
 * @author: ${USER} Date: 04.07.13 Time: 17:40
 */
public class FunStrLit extends FunctionDeclaration<StringType> {

    private final String value;

    public static FunStrLit c(String value) {
        return new FunStrLit(value);
    }

    public FunStrLit(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public AllocType<StringType> getMeta(ITypeState state) throws CpuComplexityOverflow {
        state.getCompTracker().calc(1);
        return state.getCompTracker()
                .allocLiteral(new StringType(getValue().length(), getValue().length()));
    }
}
