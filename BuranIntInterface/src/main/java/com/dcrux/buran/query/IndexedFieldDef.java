package com.dcrux.buran.query;

import com.dcrux.buran.query.indexingDef.IIndexingDef;

import java.io.Serializable;

/**
 * Buran.
 *
 * @author: ${USER} Date: 06.08.13 Time: 22:23
 */
public class IndexedFieldDef implements Serializable {
    private IndexFieldTarget fieldTarget;
    private IIndexingDef<?> indexingDef;

    public IndexedFieldDef(IndexFieldTarget fieldTarget, IIndexingDef<?> indexingDef) {
        this.fieldTarget = fieldTarget;
        this.indexingDef = indexingDef;
    }

    public static IndexedFieldDef c(IndexFieldTarget fieldTarget,
            IIndexingDef<Serializable> indexingDef) {
        return new IndexedFieldDef(fieldTarget, indexingDef);
    }

    public IndexFieldTarget getFieldTarget() {
        return fieldTarget;
    }

    public IIndexingDef<?> getIndexingDef() {
        return indexingDef;
    }
}
