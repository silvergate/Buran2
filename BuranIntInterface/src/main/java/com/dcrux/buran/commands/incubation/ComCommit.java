package com.dcrux.buran.commands.incubation;

import com.dcrux.buran.commands.Command;
import com.dcrux.buran.common.IIncNid;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Buran.
 *
 * @author: ${USER} Date: 02.07.13 Time: 16:42
 */
public class ComCommit extends Command<ICommitResult> {
    public static final Set<Class<? extends Exception>> EXCEPTIONS = exceptions();
    private Set<IIncNid> incNids;

    private ComCommit() {
    }

    public static ComCommit c(IIncNid... incNids) {
        final Set<IIncNid> incNidsSet = new HashSet<IIncNid>();
        incNidsSet.addAll(Arrays.asList(incNids));
        return new ComCommit(incNidsSet);
    }

    public ComCommit(Set<IIncNid> incNids) {
        super(EXCEPTIONS);
        this.incNids = incNids;
    }

    public Set<IIncNid> getIncNids() {
        return incNids;
    }
}
