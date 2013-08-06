package com.dcrux.buran.queryDsl.queries.unfielded;

import com.dcrux.buran.queryDsl.indexingDef.IIndexingDef;

import java.io.Serializable;

/**
 * Buran.
 *
 * @author: ${USER} Date: 06.08.13 Time: 11:15
 */
public interface ISimpleQuery<TJava extends Serializable,
        TIndexingDef extends IIndexingDef<TJava>> {

    boolean supports(TIndexingDef indexingDef);

    boolean matches(TIndexingDef indexingDef, TJava value) throws ImplementationNotDefinedException;

    boolean isImplementationDefined();
}
