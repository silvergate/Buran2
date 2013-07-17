package com.dcrux.buran.commands.incubation;

import com.dcrux.buran.commands.Command;
import com.dcrux.buran.common.IncNid;
import com.dcrux.buran.common.NidVer;

import java.util.Set;

/**
 * Buran.
 *
 * @author: ${USER} Date: 02.07.13 Time: 14:35
 */
public class ComCreateUpdate extends Command<IncNid> {
    public static final Set<Class<? extends Exception>> EXCEPTIONS = exceptions();

    private NidVer nidVer;

    private ComCreateUpdate() {
    }

    public static ComCreateUpdate c(NidVer nidVer) {
        return new ComCreateUpdate(nidVer);
    }

    public ComCreateUpdate(NidVer nidVer) {
        super(EXCEPTIONS);
        this.nidVer = nidVer;
    }

    public NidVer getNidVer() {
        return nidVer;
    }
}
