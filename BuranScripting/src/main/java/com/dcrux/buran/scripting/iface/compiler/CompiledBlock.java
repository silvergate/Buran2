package com.dcrux.buran.scripting.iface.compiler;

import java.io.Serializable;

/**
 * Buran.
 *
 * @author: ${USER} Date: 05.07.13 Time: 12:12
 */
public class CompiledBlock implements Serializable {
    private final CompiledFunction[] functions;

    public CompiledBlock(CompiledFunction[] functions) {
        this.functions = functions;
    }

    public int getNumOfLines() {
        return this.functions.length;
    }

    public CompiledFunction getFunction(int index) {
        return this.functions[index];
    }
}
