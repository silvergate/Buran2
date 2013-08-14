package com.dcrux.buran.commands.domains;

import com.dcrux.buran.common.domain.DomainHashId;
import com.dcrux.buran.common.domain.DomainId;

import java.io.Serializable;

/**
 * Buran.
 *
 * @author: ${USER} Date: 13.08.13 Time: 22:14
 */
public class DomDefineResult implements Serializable {
    private DomainId domainId;
    private DomainHashId domainHashId;

    public DomDefineResult(DomainId domainId, DomainHashId domainHashId) {
        this.domainId = domainId;
        this.domainHashId = domainHashId;
    }

    private DomDefineResult() {
    }
}
