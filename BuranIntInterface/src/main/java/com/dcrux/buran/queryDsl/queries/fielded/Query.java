package com.dcrux.buran.queryDsl.queries.fielded;

import com.dcrux.buran.queryDsl.IndexedFieldId;
import com.dcrux.buran.queryDsl.indexingDef.IIndexingDef;
import com.dcrux.buran.queryDsl.queries.IQuery;
import com.dcrux.buran.queryDsl.queries.unfielded.IMultifieldSimpleQuery;

import java.io.Serializable;

/**
 * Buran.
 *
 * @author: ${USER} Date: 06.08.13 Time: 22:30
 */
public class Query implements IOrQueryInput, IQueryOrMultifield, IQuery {
    private IndexedFieldId indexedFieldId;
    private IMultifieldSimpleQuery<Serializable, IIndexingDef<Serializable>> def;

    public Query(IndexedFieldId indexedFieldId,
            IMultifieldSimpleQuery<Serializable, IIndexingDef<Serializable>> def) {
        this.indexedFieldId = indexedFieldId;
        this.def = def;
    }

    public IndexedFieldId getIndexedFieldId() {
        return indexedFieldId;
    }

    public IMultifieldSimpleQuery<Serializable, IIndexingDef<Serializable>> getDef() {
        return def;
    }
}
