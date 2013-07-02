package com.dcrux.buran.refimpl.commands.incubation;

import com.dcrux.buran.commands.incubation.ComCommit;
import com.dcrux.buran.commands.incubation.ICommitResult;
import com.dcrux.buran.common.IIncNid;
import com.dcrux.buran.common.NidVer;
import com.dcrux.buran.refimpl.baseModules.BaseModule;
import com.dcrux.buran.refimpl.commands.TransactionalCommand;

import java.util.Map;
import java.util.Set;

/**
 * Buran.
 *
 * @author: ${USER} Date: 02.07.13 Time: 15:15
 */
public class ComCommitImpl extends TransactionalCommand<ICommitResult, ComCommit> {
    public static final ComCommitImpl SINGLETON = new ComCommitImpl();

    @Override
    public Class<ComCommit> getCommandClass() {
        return ComCommit.class;
    }

    @Override
    protected ICommitResult transactional(ComCommit command, BaseModule baseModule)
            throws Exception {
        final Map<IIncNid, NidVer> cr = baseModule.getCommitModule()
                .commit(baseModule.getAuthModule().getSender(), command.getIncNids());
        return new ICommitResult() {
            @Override
            public NidVer getNid(IIncNid incNid) {
                return cr.get(incNid);
            }

            @Override
            public Set<IIncNid> getIncNids() {
                return cr.keySet();
            }
        };
    }
}
