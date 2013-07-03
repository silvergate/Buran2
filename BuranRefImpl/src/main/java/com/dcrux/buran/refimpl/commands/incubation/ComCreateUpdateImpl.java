package com.dcrux.buran.refimpl.commands.incubation;

import com.dcrux.buran.commands.incubation.ComCreateUpdate;
import com.dcrux.buran.common.IIncNid;
import com.dcrux.buran.refimpl.baseModules.BaseModule;
import com.dcrux.buran.refimpl.commands.TransactionalCommand;

/**
 * Buran.
 *
 * @author: ${USER} Date: 02.07.13 Time: 15:15
 */
public class ComCreateUpdateImpl extends TransactionalCommand<IIncNid, ComCreateUpdate> {
    public static final ComCreateUpdateImpl SINGLETON = new ComCreateUpdateImpl();

    @Override
    protected IIncNid transactional(ComCreateUpdate command, BaseModule baseModule)
            throws Exception {
        return baseModule.getIncubationModule()
                .createIncUpdate(baseModule.getAuthModule().getSender(), command.getNidVer());
    }

    @Override
    public Class<ComCreateUpdate> getCommandClass() {
        return ComCreateUpdate.class;
    }
}
