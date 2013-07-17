package com.dcrux.buran.scripting.runner;

import com.dcrux.buran.scripting.compiler.CompiledBlock;
import com.dcrux.buran.scripting.compiler.CompiledFunction;
import com.dcrux.buran.scripting.iface.VarName;

/**
 * Buran.
 *
 * @author: ${USER} Date: 05.07.13 Time: 12:32
 */
public class Runner {

    private DataState state = new DataState();

    public void setValue(VarName varName, Object value) {
        this.state.setValue(varName, value);
    }

    public Object run(CompiledBlock block) {
        for (int i = 0; i < block.getNumOfLines(); i++) {
            final CompiledFunction fun = block.getFunction(i);

            final Object retval = fun.run(state);
            if (state.isRet()) {
                return state.getRet();
            }
            if (state.getJumpTo() != null) {
                /* Jump */
                i = state.getJumpTo() - 1;
                state.clearJumpTo();
            }
        }
        return null;
    }

    public void clearState() {
        this.state = new DataState();
    }
}
