package com.dcrux.buran.queryDsl.indexingDef;

import com.dcrux.buran.common.fields.getter.IUnfieldedDataGetter;

import java.io.Serializable;

/**
 * Buran.
 *
 * @author: ${USER} Date: 06.08.13 Time: 11:36
 */
public interface IIndexingDef<TJava extends Serializable> {
    IUnfieldedDataGetter<TJava> getDataGetter();

    boolean requiredForIndex();
}
