package com.dcrux.buran.refimpl.commands.incubation;

import com.dcrux.buran.commands.incubation.ComCreateNew;
import com.dcrux.buran.common.IncNid;
import com.dcrux.buran.refimpl.baseModules.BaseModule;
import com.dcrux.buran.refimpl.baseModules.common.IfaceUtils;
import com.dcrux.buran.refimpl.baseModules.common.OIncNid;
import com.dcrux.buran.refimpl.commands.TransactionalCommandPost;

/**
 * Buran.
 *
 * @author: ${USER} Date: 02.07.13 Time: 15:15
 */
public class ComCreateNewImpl extends TransactionalCommandPost<IncNid, ComCreateNew, OIncNid> {
    public static final ComCreateNewImpl SINGLETON = new ComCreateNewImpl();

    @Override
    protected IncNid postprocess(OIncNid oIncNid) {
        return IfaceUtils.toOutput(oIncNid);
    }

    @Override
    protected OIncNid transactional(ComCreateNew command, BaseModule baseModule) throws Exception {
        return baseModule.getIncubationModule()
                .createIncNt(baseModule.getAuthModule().getSender(), command.getPrimaryClassId());
    }

    @Override
    public Class<ComCreateNew> getCommandClass() {
        return ComCreateNew.class;
    }
}
