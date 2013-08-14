package com.dcrux.buran.refimpl.commands.domains;

import com.dcrux.buran.commands.domains.ComDomGetId;
import com.dcrux.buran.common.domain.DomainId;
import com.dcrux.buran.refimpl.commands.TransactionalCommand;
import com.dcrux.buran.refimpl.modules.BaseModule;
import com.google.common.base.Optional;

/**
 * Buran.
 *
 * @author: ${USER} Date: 13.08.13 Time: 23:33
 */
public class ComDomGetIdImpl extends TransactionalCommand<Optional<DomainId>, ComDomGetId> {

    public static final ComDomGetIdImpl SINGLETON = new ComDomGetIdImpl();

    @Override
    public Class<?> getCommandClass() {
        return ComDomGetId.class;
    }

    @Override
    public Class<BaseModule> getRunParamClass() {
        return BaseModule.class;
    }

    @Override
    protected Optional<DomainId> transactional(ComDomGetId command, BaseModule baseModule)
            throws Exception {
        return baseModule.getDomainsModule().getIdByHash(command.getDomainHashId());
    }
}
