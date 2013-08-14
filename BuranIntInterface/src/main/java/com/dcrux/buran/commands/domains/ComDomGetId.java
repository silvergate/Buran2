package com.dcrux.buran.commands.domains;

import com.dcrux.buran.commands.Command;
import com.dcrux.buran.common.domain.DomainHashId;
import com.dcrux.buran.common.domain.DomainId;
import com.google.common.base.Optional;

/**
 * Buran.
 *
 * @author: ${USER} Date: 13.08.13 Time: 22:10
 */
public class ComDomGetId extends Command<Optional<DomainId>> {
    private DomainHashId domainHashId;

    public ComDomGetId(DomainHashId domainHashId) {
        super(exceptions());
        this.domainHashId = domainHashId;
    }

    public static ComDomGetId c(DomainHashId domainHashId) {
        return new ComDomGetId(domainHashId);
    }

    private ComDomGetId() {
    }

    public DomainHashId getDomainHashId() {
        return domainHashId;
    }
}
