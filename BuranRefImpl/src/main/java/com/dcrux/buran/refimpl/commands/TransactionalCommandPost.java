package com.dcrux.buran.refimpl.commands;

import com.dcrux.buran.commandBase.ICommand;
import com.dcrux.buran.refimpl.commandDispatchBase.ICommandImpl;
import com.dcrux.buran.refimpl.modules.BaseModule;
import com.orientechnologies.orient.core.db.document.ODatabaseDocument;
import com.orientechnologies.orient.core.tx.OTransaction;

import java.io.Serializable;

/**
 * Buran.
 *
 * @author: ${USER} Date: 02.07.13 Time: 15:16
 */
public abstract class TransactionalCommandPost<TRetval extends Serializable,
        TCommand extends ICommand<TRetval>, TIntermediate>
        implements ICommandImpl<BaseModule, TRetval, TCommand> {

    @Override
    public Class<BaseModule> getRunParamClass() {
        return BaseModule.class;
    }

    @Override
    public final TRetval run(TCommand command, BaseModule baseModule) throws Exception {
        final ODatabaseDocument database = baseModule.getDb();
        database.begin(OTransaction.TXTYPE.OPTIMISTIC);
        TIntermediate retval;
        try {
            retval = transactional(command, baseModule);
            database.commit();
        } catch (Throwable t) {
            database.rollback();
            throw t;
        }
        return postprocess(retval);
    }

    protected abstract TRetval postprocess(TIntermediate intermediate);

    protected abstract TIntermediate transactional(TCommand command, BaseModule baseModule)
            throws Exception;
}
