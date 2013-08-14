package com.dcrux.buran.refimpl.commands.dataFetch;

import com.dcrux.buran.commands.dataFetch.ComFetch;
import com.dcrux.buran.common.Nid;
import com.dcrux.buran.common.NidVer;
import com.dcrux.buran.refimpl.commands.TransactionalCommand;
import com.dcrux.buran.refimpl.modules.BaseModule;
import com.dcrux.buran.refimpl.modules.common.IfaceUtils;
import com.dcrux.buran.refimpl.modules.common.ONidVer;

import java.io.Serializable;

/**
 * Buran.
 *
 * @author: ${USER} Date: 02.07.13 Time: 18:37
 */
public class ComFetchImpl<TRetVal extends Serializable>
        extends TransactionalCommand<TRetVal, ComFetch<TRetVal>> {
    public static final ComFetchImpl SINGLETON = new ComFetchImpl();

    @Override
    protected TRetVal transactional(ComFetch<TRetVal> command, BaseModule baseModule)
            throws Exception {
        final ONidVer nidVer;
        if (command.getNidVer() instanceof NidVer) {
            nidVer = IfaceUtils.toInput((NidVer) command.getNidVer());
        } else {
            nidVer = baseModule.getDataFetchModule().toNidVer((Nid) command.getNidVer());
        }
        return baseModule.getDataFetchModule().getData(nidVer, command.getGetter());
    }

    @Override
    public Class<?> getCommandClass() {
        return ComFetch.class;

    }
}