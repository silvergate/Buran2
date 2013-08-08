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


}
