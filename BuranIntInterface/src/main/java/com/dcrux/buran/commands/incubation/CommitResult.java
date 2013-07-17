package com.dcrux.buran.commands.incubation;

import com.dcrux.buran.common.IncNid;
import com.dcrux.buran.common.NidVer;
import com.sun.istack.internal.Nullable;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

/**
 * Buran.
 *
 * @author: ${USER} Date: 17.07.13 Time: 20:03
 */
public class CommitResult implements Serializable {

    private Map<IncNid, NidVer> incNidNidVerMap;

    public CommitResult(Map<IncNid, NidVer> incNidNidVerMap) {
        this.incNidNidVerMap = incNidNidVerMap;
    }

    private CommitResult() {
    }

    @Nullable
    public NidVer getNid(IncNid incNid) {
        return this.incNidNidVerMap.get(incNid);
    }

    public Set<IncNid> getIncNids() {
        return this.incNidNidVerMap.keySet();
    }
}
