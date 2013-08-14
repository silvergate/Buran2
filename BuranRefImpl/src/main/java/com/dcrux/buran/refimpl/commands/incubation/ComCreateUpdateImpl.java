package com.dcrux.buran.refimpl.commands.incubation;

import com.dcrux.buran.commands.incubation.ComCreateUpdate;
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
public class ComCreateUpdateImpl
        extends TransactionalCommandPost<IncNid, ComCreateUpdate, OIncNid> {
    public static final ComCreateUpdateImpl SINGLETON = new ComCreateUpdateImpl();

    @Override
    protected IncNid postprocess(OIncNid oIncNid) {
        return IfaceUtils.toOutput(oIncNid);
    }

    @Override
    protected OIncNid transactional(ComCreateUpdate command, BaseModule baseModule)
            throws Exception {
        return baseModule.getIncubationModule()
                .createIncUpdate(baseModule.getAuthModule().getSender(),
                        IfaceUtils.toInput(command.getNidVer()));
    }

    @Override
    public Class<ComCreateUpdate> getCommandClass() {
        return ComCreateUpdate.class;
    }
}
