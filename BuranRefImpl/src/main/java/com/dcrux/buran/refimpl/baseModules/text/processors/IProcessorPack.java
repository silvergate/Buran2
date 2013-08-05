package com.dcrux.buran.refimpl.baseModules.text.processors;

/**
 * Buran.
 *
 * @author: ${USER} Date: 05.08.13 Time: 21:48
 */
public interface IProcessorPack extends IKeyGen {
    void process(String input, IEmmitter.ICallback callback);
}
