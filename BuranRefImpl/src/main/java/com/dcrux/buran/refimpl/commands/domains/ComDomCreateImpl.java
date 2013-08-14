package com.dcrux.buran.refimpl.commands.domains;

import com.dcrux.buran.commands.domains.ComDomCreate;
import com.dcrux.buran.common.domain.DomainId;
import com.dcrux.buran.refimpl.commandDispatchBase.ICommandImpl;
import com.dcrux.buran.refimpl.modules.BaseModule;

/**
 * Buran.
 *
 * @author: ${USER} Date: 13.08.13 Time: 23:31
 */
public class ComDomCreateImpl implements ICommandImpl<BaseModule, DomainId, ComDomCreate> {

    public static final ComDomCreateImpl SINGLETON = new ComDomCreateImpl();

    @Override
    public Class<?> getCommandClass() {
        return ComDomCreate.class;
    }

    @Override
    public Class<BaseModule> getRunParamClass() {
        return BaseModule.class;
    }

    @Override
    public DomainId run(ComDomCreate command, BaseModule baseModule) throws Exception {
        return baseModule.getDomainsModule().createDomain();
    }
}
