package com.dcrux.buran.commands.domains;

import com.dcrux.buran.commands.Command;
import com.dcrux.buran.common.domain.DomainId;

/**
 * Buran.
 *
 * @author: ${USER} Date: 13.08.13 Time: 22:10
 */
public class ComDomCreate extends Command<DomainId> {

    public static ComDomCreate SINGLETON = new ComDomCreate();

    private ComDomCreate() {
        super(exceptions());
    }

}
