package com.dcrux.buran.refimpl.commands.incubation;

import com.dcrux.buran.commands.incubation.ComCreateNew;
import com.dcrux.buran.common.IncNid;
import com.dcrux.buran.refimpl.commands.TransactionalCommandPost;
import com.dcrux.buran.refimpl.modules.BaseModule;
import com.dcrux.buran.refimpl.modules.common.IfaceUtils;
import com.dcrux.buran.refimpl.modules.common.OIncNid;

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
