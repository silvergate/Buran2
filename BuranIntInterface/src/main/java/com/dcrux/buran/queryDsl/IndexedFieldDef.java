package com.dcrux.buran.queryDsl;

import com.dcrux.buran.common.classDefinition.DependencyIndex;
import com.dcrux.buran.common.fields.FieldIndex;
import com.dcrux.buran.common.fields.IFieldTarget;
import com.dcrux.buran.queryDsl.indexingDef.IIndexingDef;
import com.google.common.base.Optional;

import java.io.Serializable;

/**
 * Buran.
 *
 * @author: ${USER} Date: 06.08.13 Time: 22:23
 */
public class IndexedFieldDef {
    /* Point to other class */
    private Optional<DependencyIndex> dependencyIndex;
    /* Link that points to ther node to index */
    private Optional<FieldIndex> linkIndex;

    private IFieldTarget fieldTarget;
    private IIndexingDef<Serializable> indexingDef;

    public IndexedFieldDef(Optional<DependencyIndex> dependencyIndex, IFieldTarget fieldTarget,
            IIndexingDef<Serializable> indexingDef) {
        this.dependencyIndex = dependencyIndex;
        this.fieldTarget = fieldTarget;
        this.indexingDef = indexingDef;
    }

    public static IndexedFieldDef c(IFieldTarget fieldTarget,
            IIndexingDef<Serializable> indexingDef) {
        return new IndexedFieldDef(Optional.<DependencyIndex>absent(), fieldTarget, indexingDef);
    }

    public Optional<DependencyIndex> getDependencyIndex() {
        return dependencyIndex;
    }

    public IFieldTarget getFieldTarget() {
        return fieldTarget;
    }

    public IIndexingDef<Serializable> getIndexingDef() {
        return indexingDef;
    }

    public Optional<FieldIndex> getLinkIndex() {
        return linkIndex;
    }
}
