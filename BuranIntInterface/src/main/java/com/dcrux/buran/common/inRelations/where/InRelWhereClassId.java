package com.dcrux.buran.common.inRelations.where;

import com.dcrux.buran.common.classes.ClassId;
import com.dcrux.buran.common.fields.FieldIndex;
import com.google.common.base.Optional;

/**
 * Buran.
 *
 * @author: ${USER} Date: 04.08.13 Time: 22:52
 */
public class InRelWhereClassId implements IInRelationWhere {
    private ClassId sourceClassId;
    private Optional<FieldIndex> startIndex;
    private Optional<FieldIndex> endIndex;

    public InRelWhereClassId(ClassId sourceClassId, Optional<FieldIndex> startIndex,
            Optional<FieldIndex> endIndex) {
        this.sourceClassId = sourceClassId;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

    public static InRelWhereClassId withFieldIndex(ClassId sourceClassId, FieldIndex fieldIndex) {
        return new InRelWhereClassId(sourceClassId, Optional.of(fieldIndex),
                Optional.of(fieldIndex));
    }

    private InRelWhereClassId() {
    }

    public ClassId getSourceClassId() {
        return sourceClassId;
    }

    public Optional<FieldIndex> getStartIndex() {
        return startIndex;
    }

    public Optional<FieldIndex> getEndIndex() {
        return endIndex;
    }
}
