package com.dcrux.buran.query.queries;

import com.dcrux.buran.common.classDefinition.ClassIndexId;
import com.dcrux.buran.common.classes.ClassId;
import com.dcrux.buran.query.IndexedFieldId;

/**
 * Buran.
 *
 * @author: ${USER} Date: 08.08.13 Time: 15:28
 */
public class QueryTarget {
    private ClassId classId;
    private ClassIndexId indexId;
    private IndexedFieldId indexedFieldId;

    public QueryTarget(ClassId classId, ClassIndexId indexId, IndexedFieldId indexedFieldId) {
        this.classId = classId;
        this.indexId = indexId;
        this.indexedFieldId = indexedFieldId;
    }

    public static QueryTarget cDef(ClassId classId, IndexedFieldId indexedFieldId) {
        return new QueryTarget(classId, ClassIndexId.DEFAULT, indexedFieldId);
    }

    public static QueryTarget c(ClassId classId, ClassIndexId classIndexId,
            IndexedFieldId indexedFieldId) {
        return new QueryTarget(classId, classIndexId, indexedFieldId);
    }

    public ClassId getClassId() {
        return classId;
    }

    public ClassIndexId getIndexId() {
        return indexId;
    }

    public IndexedFieldId getIndexedFieldId() {
        return indexedFieldId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        QueryTarget that = (QueryTarget) o;

        if (!classId.equals(that.classId)) return false;
        if (!indexId.equals(that.indexId)) return false;
        if (!indexedFieldId.equals(that.indexedFieldId)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = classId.hashCode();
        result = 31 * result + indexId.hashCode();
        result = 31 * result + indexedFieldId.hashCode();
        return result;
    }
}
