package com.dcrux.buran.common.query.queries.unfielded;

import com.dcrux.buran.common.query.indexingDef.IIndexingDef;

import java.io.Serializable;

/**
 * Buran.
 *
 * @author: ${USER} Date: 06.08.13 Time: 11:15
 */
public interface IMultifieldSimpleQuery<TJava extends Serializable,
        TIndexingDef extends IIndexingDef<TJava>>
        extends ISimpleQuery<TJava, TIndexingDef> {
}
