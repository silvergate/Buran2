package com.dcrux.buran.refimpl.commands.subscription;

import com.dcrux.buran.commands.subscription.ComAddSub;
import com.dcrux.buran.common.subscription.SubId;
import com.dcrux.buran.refimpl.baseModules.BaseModule;
import com.dcrux.buran.refimpl.commands.TransactionalCommand;
import com.dcrux.buran.refimpl.subscription.subRegistry.SubRegistry;

/**
 * Buran.
 *
 * @author: ${USER} Date: 02.07.13 Time: 15:15
 */
public class ComAddSubImpl extends TransactionalCommand<SubId, ComAddSub> {
    public static final ComAddSubImpl SINGLETON = new ComAddSubImpl();

    @Override
    protected SubId transactional(ComAddSub command, BaseModule baseModule) throws Exception {
        final SubRegistry subRegistry =
                baseModule.getSubscriptionModule().getOrCreateRegistry(baseModule.getReceiver());
        final SubId subId =
                subRegistry.addSubscription(command.getBlockId(), command.getSubDefinition());
        return subId;
    }

    @Override
    public Class<ComAddSub> getCommandClass() {
        return ComAddSub.class;
    }
}
