package com.dcrux.buran.query.queries.fielded;

import com.dcrux.buran.query.indexingDef.IIndexingDef;
import com.dcrux.buran.query.queries.IQuery;
import com.dcrux.buran.query.queries.QueryTarget;
import com.dcrux.buran.query.queries.unfielded.ISimpleQuery;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Buran.
 *
 * @author: ${USER} Date: 06.08.13 Time: 22:30
 */
public class MultiFieldQuery implements IOrQueryInput, IQueryOrMultifield, IQuery {
    private Set<QueryTarget> indexedFieldIds;
    private ISimpleQuery<Serializable, IIndexingDef<Serializable>> def;

    public MultiFieldQuery(ISimpleQuery<Serializable, IIndexingDef<Serializable>> def,
            QueryTarget... indexedFieldIds) {
        this.def = def;
        this.indexedFieldIds = new HashSet<QueryTarget>();
        this.indexedFieldIds.addAll(Arrays.asList(indexedFieldIds));
    }

    public Set<QueryTarget> getIndexedFieldIds() {
        return indexedFieldIds;
    }

    public ISimpleQuery<Serializable, IIndexingDef<Serializable>> getDef() {
        return def;
    }
}
