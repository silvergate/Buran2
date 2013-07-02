package com.dcrux.buran.refimpl.commands.incubation;

import com.dcrux.buran.commands.incubation.ComCreateNew;
import com.dcrux.buran.common.IIncNid;
import com.dcrux.buran.refimpl.baseModules.BaseModule;
import com.dcrux.buran.refimpl.commands.TransactionalCommand;

/**
 * Buran.
 *
 * @author: ${USER} Date: 02.07.13 Time: 15:15
 */
public class ComCreateNewImpl extends TransactionalCommand<IIncNid, ComCreateNew> {
    public static final ComCreateNewImpl SINGLETON = new ComCreateNewImpl();

    @Override
    protected IIncNid transactional(ComCreateNew command, BaseModule baseModule) throws Exception {
        return baseModule.getIncubationModule()
                .createIncNt(baseModule.getAuthModule().getSender(), command.getClassId());
    }

    @Override
    public Class<ComCreateNew> getCommandClass() {
        return ComCreateNew.class;
    }
}
