package com.dcrux.buran.refimpl.commands.dataFetch;

import com.dcrux.buran.commands.dataFetch.ComFetch;
import com.dcrux.buran.refimpl.baseModules.BaseModule;
import com.dcrux.buran.refimpl.baseModules.common.IfaceUtils;
import com.dcrux.buran.refimpl.commands.TransactionalCommand;

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
        return baseModule.getDataFetchModule()
                .getData(IfaceUtils.toInput(command.getNidVer()), command.getGetter());
    }

    @Override
    public Class<?> getCommandClass() {
        return ComFetch.class;

    }
}