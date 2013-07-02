package com.dcrux.buran.refimpl.commands.dataFetch;

import com.dcrux.buran.commands.dataFetch.ComFetch;
import com.dcrux.buran.commands.dataFetch.FetchResult;
import com.dcrux.buran.refimpl.baseModules.BaseModule;
import com.dcrux.buran.refimpl.commands.TransactionalCommand;

import java.io.Serializable;

/**
 * Buran.
 *
 * @author: ${USER} Date: 02.07.13 Time: 18:37
 */
public class ComFetchImpl<TFieldResult extends Serializable, TLabelResult extends Serializable>
        extends
        TransactionalCommand<FetchResult<TFieldResult, TLabelResult>, ComFetch<TFieldResult,
                TLabelResult>> {
    public static final ComFetchImpl SINGLETON = new ComFetchImpl();

    @Override
    protected FetchResult transactional(ComFetch command, BaseModule baseModule) throws Exception {
        return baseModule.getDataFetchModule()
                .getData(command.getNidVer(), command.getFieldGetter(), command.getLabelGetter());
    }


    @Override
    public Class<?> getCommandClass() {
        return ComFetch.class;

    }
}