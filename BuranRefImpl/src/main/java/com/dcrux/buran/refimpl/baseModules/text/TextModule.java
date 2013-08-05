package com.dcrux.buran.refimpl.baseModules.text;

import com.dcrux.buran.refimpl.baseModules.BaseModule;
import com.dcrux.buran.refimpl.baseModules.common.Module;
import com.dcrux.buran.refimpl.baseModules.text.processors.IEmmitter;
import com.dcrux.buran.refimpl.baseModules.text.processors.KeyRange;
import com.dcrux.buran.refimpl.baseModules.text.processors.impl.enUK.ProcessorPack;
import com.sun.istack.internal.Nullable;

/**
 * Buran.
 *
 * @author: ${USER} Date: 05.08.13 Time: 12:27
 */
public class TextModule extends Module<BaseModule> {
    public TextModule(BaseModule baseModule) {
        super(baseModule);
    }

    public void process(String input, IEmmitter.ICallback callback) {
        ProcessorPack processorPack = new ProcessorPack();
        processorPack.process(input, callback);
    }

    @Nullable
    public KeyRange generateKey(String query) {
        ProcessorPack processorPack = new ProcessorPack();
        return processorPack.generateKey(query);
    }
}
