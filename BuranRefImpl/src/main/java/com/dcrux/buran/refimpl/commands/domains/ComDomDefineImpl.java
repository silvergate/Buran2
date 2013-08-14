package com.dcrux.buran.refimpl.commands.domains;

import com.dcrux.buran.commands.domains.ComDomDefine;
import com.dcrux.buran.commands.domains.DomDefineResult;
import com.dcrux.buran.refimpl.commandDispatchBase.ICommandImpl;
import com.dcrux.buran.refimpl.modules.BaseModule;

/**
 * Buran.
 *
 * @author: ${USER} Date: 13.08.13 Time: 23:33
 */
public class ComDomDefineImpl implements ICommandImpl<BaseModule, DomDefineResult, ComDomDefine> {

    public static final ComDomDefineImpl SINGLETON = new ComDomDefineImpl();

    @Override
    public Class<?> getCommandClass() {
        return ComDomDefine.class;
    }

    @Override
    public Class<BaseModule> getRunParamClass() {
        return BaseModule.class;
    }

    @Override
    public DomDefineResult run(ComDomDefine command, BaseModule baseModule) throws Exception {
        return baseModule.getDomainsModule().defineDomain(command.getDomainDef());
    }
}
