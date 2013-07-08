package com.dcrux.buran.refimpl.commands.dataMut;

import com.dcrux.buran.commandBase.VoidType;
import com.dcrux.buran.commands.dataMut.ComMutate;
import com.dcrux.buran.refimpl.baseModules.BaseModule;
import com.dcrux.buran.refimpl.baseModules.changeTracker.NullChangeTracker;
import com.dcrux.buran.refimpl.commands.TransactionalCommand;

/**
 * Buran.
 *
 * @author: ${USER} Date: 02.07.13 Time: 17:48
 */
public class ComMutateImpl extends TransactionalCommand<VoidType, ComMutate> {
    public static final ComMutateImpl SINGLETON = new ComMutateImpl();

    @Override
    public Class<ComMutate> getCommandClass() {
        return ComMutate.class;
    }

    @Override
    protected VoidType transactional(ComMutate command, BaseModule baseModule) throws Exception {
        baseModule.getDataMutModule()
                .setData(baseModule.getAuthModule().getSender(), command.getIncNid(),
                        command.getSetter(), NullChangeTracker.SINGLETON);
        return VoidType.SINGLETON;
    }
}
