package com.dcrux.buran.refimpl.modules.fields;

import com.orientechnologies.orient.core.id.ORID;
import com.sun.istack.internal.Nullable;

/**
 * Buran.
 *
 * @author: ${USER} Date: 04.08.13 Time: 19:40
 */
public interface ICommitInfo {
    @Nullable
    ORID getNidVerByIncNid(ORID incNid);

    @Nullable
    ORID getNidByIncNid(ORID incNid);
}
