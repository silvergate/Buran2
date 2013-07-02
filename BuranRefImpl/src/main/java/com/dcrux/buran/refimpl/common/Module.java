package com.dcrux.buran.refimpl.common;

/**
 * Buran.
 *
 * @author: ${USER} Date: 02.07.13 Time: 03:57
 */
public class Module<TBaseModule extends Object> {
    private final TBaseModule module;

    public Module(TBaseModule module) {
        this.module = module;
    }

    public final TBaseModule getBase() {
        return module;
    }
}
