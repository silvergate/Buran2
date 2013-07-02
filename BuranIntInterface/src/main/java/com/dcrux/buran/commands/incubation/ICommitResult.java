package com.dcrux.buran.commands.incubation;

import com.dcrux.buran.common.IIncNid;
import com.dcrux.buran.common.NidVer;
import com.sun.istack.internal.Nullable;

import java.io.Serializable;
import java.util.Set;

/**
 * Buran.
 *
 * @author: ${USER} Date: 02.07.13 Time: 16:49
 */
public interface ICommitResult extends Serializable {
    @Nullable
    NidVer getNid(IIncNid incNid);

    Set<IIncNid> getIncNids();
}
