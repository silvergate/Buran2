package com.dcrux.buran.refimpl.commands.subscription;

import com.dcrux.buran.commands.subscription.ComAddSub;
import com.dcrux.buran.common.subscription.SubId;
import com.dcrux.buran.refimpl.baseModules.BaseModule;
import com.dcrux.buran.refimpl.commands.TransactionalCommand;

/**
 * Buran.
 *
 * @author: ${USER} Date: 02.07.13 Time: 15:15
 */
public class ComAddSubImpl extends TransactionalCommand<SubId, ComAddSub> {
    public static final ComAddSubImpl SINGLETON = new ComAddSubImpl();

    @Override
    protected SubId transactional(ComAddSub command, BaseModule baseModule) throws Exception {
        //TODO: Implement me
        return null;
    }

    @Override
    public Class<ComAddSub> getCommandClass() {
        return ComAddSub.class;
    }
}
