package com.dcrux.buran.refimpl.commands.incubation;

import com.dcrux.buran.commands.incubation.ComCommit;
import com.dcrux.buran.commands.incubation.ICommitResult;
import com.dcrux.buran.common.IIncNid;
import com.dcrux.buran.common.NidVer;
import com.dcrux.buran.refimpl.baseModules.BaseModule;
import com.dcrux.buran.refimpl.baseModules.commit.CommitResult;
import com.dcrux.buran.refimpl.commandDispatchBase.ICommandImpl;
import com.orientechnologies.orient.core.db.document.ODatabaseDocument;
import com.orientechnologies.orient.core.tx.OTransaction;

import java.util.Set;

/**
 * Buran.
 *
 * @author: ${USER} Date: 02.07.13 Time: 15:15
 */
public class ComCommitImpl implements ICommandImpl<BaseModule, ICommitResult, ComCommit> {
    public static final ComCommitImpl SINGLETON = new ComCommitImpl();

    @Override
    public Class<ComCommit> getCommandClass() {
        return ComCommit.class;
    }

    @Override
    public Class<BaseModule> getRunParamClass() {
        return BaseModule.class;
    }

    @Override
    public ICommitResult run(ComCommit command, BaseModule baseModule) throws Exception {
        final ODatabaseDocument database = baseModule.getDb();
        database.begin(OTransaction.TXTYPE.OPTIMISTIC);

        /* Commit nodes */
        final CommitResult commitResult;
        try {
            commitResult = transactionOne(command, baseModule);
            database.commit();
        } catch (Throwable t) {
            database.rollback();
            throw t;
        }

        /* Update indexes */
        //TODO: Catch exceptions (exceptions should never occur)
        for (CommitResult.IndexResult addToIndex : commitResult.getAddToIndexes()) {
            baseModule.getIndexModule()
                    .index(addToIndex.getVersionsRecord(), addToIndex.getClassId());
        }
        for (CommitResult.IndexResult removeFromIndex : commitResult
                .getRemoveFromIndexesCauseRemoved()) {
            baseModule.getIndexModule().removeFromIndex(removeFromIndex.getVersionsRecord(),
                    removeFromIndex.getClassId(), true);
        }
        for (CommitResult.IndexResult removeFromIndex : commitResult
                .getRemoveFromIndexesCauseUpdated()) {
            baseModule.getIndexModule().removeFromIndex(removeFromIndex.getVersionsRecord(),
                    removeFromIndex.getClassId(), false);
        }

        return new ICommitResult() {
            @Override
            public NidVer getNid(IIncNid incNid) {
                return commitResult.getNidVersMap().get(incNid);
            }

            @Override
            public Set<IIncNid> getIncNids() {
                return commitResult.getNidVersMap().keySet();
            }
        };
    }

    private CommitResult transactionOne(ComCommit command, BaseModule baseModule) throws Exception {
        final CommitResult cr = baseModule.getCommitModule()
                .commit(baseModule.getAuthModule().getSender(), command.getIncNids());
        return cr;
    }
}
