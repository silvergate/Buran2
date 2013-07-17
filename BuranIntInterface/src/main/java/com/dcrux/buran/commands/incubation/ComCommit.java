package com.dcrux.buran.commands.incubation;

import com.dcrux.buran.commands.Command;
import com.dcrux.buran.common.IncNid;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Buran.
 *
 * @author: ${USER} Date: 02.07.13 Time: 16:42
 */
public class ComCommit extends Command<CommitResult> {
    public static final Set<Class<? extends Exception>> EXCEPTIONS = exceptions();
    private Set<IncNid> incNids;

    private ComCommit() {
    }

    public static ComCommit c(IncNid... incNids) {
        final Set<IncNid> incNidsSet = new HashSet<IncNid>();
        incNidsSet.addAll(Arrays.asList(incNids));
        return new ComCommit(incNidsSet);
    }

    public ComCommit(Set<IncNid> incNids) {
        super(EXCEPTIONS);
        this.incNids = incNids;
    }

    public Set<IncNid> getIncNids() {
        return incNids;
    }
}
