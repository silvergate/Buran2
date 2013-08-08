package com.dcrux.buran.common.fields;

import com.dcrux.buran.common.classes.ClassId;
import com.dcrux.buran.utils.AltType;

/**
 * Buran.
 *
 * @author: ${USER} Date: 07.08.13 Time: 00:58
 */
public class FieldTarget extends AltType<IFieldTarget> implements IFieldTarget {
    private ClassId classId;
    private FieldIndex fieldIndex;

    public FieldTarget(ClassId classId, FieldIndex fieldIndex) {
        this.classId = classId;
        this.fieldIndex = fieldIndex;
    }

    public static FieldTarget c(ClassId classId, FieldIndex fieldIndex) {
        return new FieldTarget(classId, fieldIndex);
    }

    private FieldTarget() {
    }

    public ClassId getClassId() {
        return classId;
    }

    public FieldIndex getFieldIndex() {
        return fieldIndex;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FieldTarget that = (FieldTarget) o;

        if (!classId.equals(that.classId)) return false;
        if (!fieldIndex.equals(that.fieldIndex)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = classId.hashCode();
        result = 31 * result + fieldIndex.hashCode();
        return result;
    }
}
