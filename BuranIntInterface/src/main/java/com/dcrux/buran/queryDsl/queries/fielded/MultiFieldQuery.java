package com.dcrux.buran.queryDsl.queries.fielded;

import com.dcrux.buran.queryDsl.IndexedFieldId;
import com.dcrux.buran.queryDsl.indexingDef.IIndexingDef;
import com.dcrux.buran.queryDsl.queries.IQuery;
import com.dcrux.buran.queryDsl.queries.unfielded.ISimpleQuery;

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
    private Set<IndexedFieldId> indexedFieldIds;
    private ISimpleQuery<Serializable, IIndexingDef<Serializable>> def;

    public MultiFieldQuery(ISimpleQuery<Serializable, IIndexingDef<Serializable>> def,
            IndexedFieldId... indexedFieldIds) {
        this.def = def;
        this.indexedFieldIds = new HashSet<IndexedFieldId>();
        this.indexedFieldIds.addAll(Arrays.asList(indexedFieldIds));
    }

    public Set<IndexedFieldId> getIndexedFieldIds() {
        return indexedFieldIds;
    }

    public ISimpleQuery<Serializable, IIndexingDef<Serializable>> getDef() {
        return def;
    }
}
