package com.dcrux.buran.refimpl.commandDispatchBase;

/**
 * Buran.
 *
 * @author: ${USER} Date: 02.07.13 Time: 14:49
 */
public interface ICommandRunParamProvider<TRunParam extends Object> {
    TRunParam getRunParam();
}
