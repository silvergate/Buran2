package com.dcrux.buran.commands.domains;

import com.dcrux.buran.commands.Command;
import com.dcrux.buran.common.domain.DomainDef;

/**
 * Buran.
 *
 * @author: ${USER} Date: 13.08.13 Time: 22:10
 */
public class ComDomDefine extends Command<DomDefineResult> {
    private DomainDef domainDef;

    public ComDomDefine(DomainDef domainDef) {
        super(exceptions());
        this.domainDef = domainDef;
    }

    public static ComDomDefine c(DomainDef domainDef) {
        return new ComDomDefine(domainDef);
    }

    private ComDomDefine() {
    }

    public DomainDef getDomainDef() {
        return domainDef;
    }
}
