package com.dcrux.buran.refimpl.commands.indexingNew;

import com.dcrux.buran.commands.indexing.QueryResult;
import com.dcrux.buran.commands.indexingNew.ComQueryNew;
import com.dcrux.buran.common.UserId;
import com.dcrux.buran.refimpl.commandDispatchBase.ICommandImpl;
import com.dcrux.buran.refimpl.modules.BaseModule;

/**
 * Buran.
 *
 * @author: ${USER} Date: 09.07.13 Time: 22:05
 */
public class ComQueryNewImpl implements ICommandImpl<BaseModule, QueryResult, ComQueryNew> {

    public static final ComQueryNewImpl SINGLETON = new ComQueryNewImpl();

    @Override
    public Class<?> getCommandClass() {
        return ComQueryNew.class;
    }

    @Override
    public Class<BaseModule> getRunParamClass() {
        return BaseModule.class;
    }

    @Override
    public QueryResult run(final ComQueryNew command, final BaseModule baseModule)
            throws Exception {
        final UserId receiver = baseModule.getAuthModule().getReceiver();
        baseModule.getSearchModule().search(receiver, command.getQuery());
        return null;
    }
}
