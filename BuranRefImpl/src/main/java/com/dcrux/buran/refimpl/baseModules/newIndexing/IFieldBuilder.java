package com.dcrux.buran.refimpl.baseModules.newIndexing;

import com.dcrux.buran.common.UserId;
import com.dcrux.buran.common.query.queries.QueryTarget;

/**
 * Buran.
 *
 * @author: ${USER} Date: 08.08.13 Time: 15:14
 */
public interface IFieldBuilder {
    String getField(UserId receiver, QueryTarget target);

    String getIndex(UserId userId);

    String getType();
}
