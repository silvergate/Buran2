package com.dcrux.buran.refimpl.commands.incubation;

import com.dcrux.buran.commands.incubation.ComCommit;
import com.dcrux.buran.common.IncNid;
import com.dcrux.buran.common.NidVer;
import com.dcrux.buran.common.UserId;
import com.dcrux.buran.refimpl.baseModules.BaseModule;
import com.dcrux.buran.refimpl.baseModules.commit.CommitResult;
import com.dcrux.buran.refimpl.baseModules.common.IfaceUtils;
import com.dcrux.buran.refimpl.baseModules.common.ONidVer;
import com.dcrux.buran.refimpl.commandDispatchBase.ICommandImpl;
import com.orientechnologies.orient.core.db.document.ODatabaseDocument;
import com.orientechnologies.orient.core.tx.OTransaction;

import java.util.HashMap;
import java.util.Map;

/**
 * Buran.
 *
 * @author: ${USER} Date: 02.07.13 Time: 15:15
 */
public class ComCommitImpl implements
        ICommandImpl<BaseModule, com.dcrux.buran.commands.incubation.CommitResult, ComCommit> {
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
    public com.dcrux.buran.commands.incubation.CommitResult run(ComCommit command,
            BaseModule baseModule) throws Exception {
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

        final UserId receiver = baseModule.getAuthModule().getReceiver();
        /* Update indexes */
        //TODO: Catch exceptions (exceptions should never occur)
        for (CommitResult.IndexResult addToIndex : commitResult.getAddToIndexes()) {
            /*baseModule.getIndexModule()
                    .index(addToIndex.getVersionsRecord(), addToIndex.getPrimaryClassId());*/
            baseModule.getIndexingModule()
                    .index(receiver, addToIndex.getVersionsRecord(), addToIndex.getClassId());
        }
        for (CommitResult.IndexResult removeFromIndex : commitResult
                .getRemoveFromIndexesCauseRemoved()) {
            /*baseModule.getIndexModule()
                    .removeFromIndex(removeFromIndex.getVersionsRecord().getoIdentifiable(),
                            removeFromIndex.getPrimaryClassId(), true);*/
            baseModule.getIndexingModule().removeFromIndex(receiver,
                    removeFromIndex.getVersionsRecord().getoIdentifiable(),
                    removeFromIndex.getClassId(), true);
        }
        for (CommitResult.IndexResult removeFromIndex : commitResult
                .getRemoveFromIndexesCauseUpdated()) {
            /*baseModule.getIndexModule()
                    .removeFromIndex(removeFromIndex.getVersionsRecord().getoIdentifiable(),
                            removeFromIndex.getPrimaryClassId(), false);*/
            baseModule.getIndexingModule().removeFromIndex(receiver,
                    removeFromIndex.getVersionsRecord().getoIdentifiable(),
                    removeFromIndex.getClassId(), false);
        }

        /* Prepare commit result */
        final Map<IncNid, NidVer> resultMap = new HashMap<>();
        for (final Map.Entry<IncNid, ONidVer> entry : commitResult.getNidVersMap().entrySet()) {
            resultMap.put(entry.getKey(), IfaceUtils.toOutput(entry.getValue()));
        }
        return new com.dcrux.buran.commands.incubation.CommitResult(resultMap);
    }

    private CommitResult transactionOne(ComCommit command, BaseModule baseModule) throws Exception {
        final CommitResult cr = baseModule.getCommitModule()
                .commit(baseModule.getAuthModule().getSender(), command.getIncNids());
        return cr;
    }
}
