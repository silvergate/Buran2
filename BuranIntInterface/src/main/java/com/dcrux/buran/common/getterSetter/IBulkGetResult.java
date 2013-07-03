package com.dcrux.buran.common.getterSetter;

import java.io.Serializable;

/**
 * Buran.
 *
 * @author: ${USER} Date: 03.07.13 Time: 02:18
 */
public interface IBulkGetResult extends Serializable {
    <TRetVal> TRetVal get(BulkGetIndex<TRetVal> index);
}
