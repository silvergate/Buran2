package com.dcrux.buran.scripting.iface.runner;

import com.dcrux.buran.scripting.iface.compiler.CompiledBlock;
import com.dcrux.buran.scripting.iface.compiler.CompiledFunction;

/**
 * Buran.
 *
 * @author: ${USER} Date: 05.07.13 Time: 12:32
 */
public class Runner {
    public Object run(CompiledBlock block) {
        DataState state = new DataState();
        for (int i = 0; i < block.getNumOfLines(); i++) {
            final CompiledFunction fun = block.getFunction(i);

            final Object retval = fun.run(state);
            if (state.isRet()) {
                return state.getRet();
            }
            if (state.getJumpTo() != null) {
                //System.out.println("Jump to line: " + state.getJumpTo());
                /* Jump */
                i = state.getJumpTo() - 1;
                state.clearJumpTo();
            }
        }
        return null;
    }
}
