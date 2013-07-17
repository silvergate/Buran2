package com.dcrux.buran.refimpl.subscription.subRegistry;

import com.dcrux.buran.common.classDefinition.ClassIndexName;
import com.dcrux.buran.common.classes.ClassId;

/**
 * Buran.
 *
 * @author: ${USER} Date: 17.07.13 Time: 02:09
 */
class ClassIdAndIndex {
    private final ClassId classId;
    private final ClassIndexName classIndexName;

    public ClassIdAndIndex(ClassId classId, ClassIndexName classIndexName) {
        this.classId = classId;
        this.classIndexName = classIndexName;
    }

    public ClassId getClassId() {
        return classId;
    }

    public ClassIndexName getClassIndexName() {
        return classIndexName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClassIdAndIndex that = (ClassIdAndIndex) o;

        if (!classId.equals(that.classId)) return false;
        if (!classIndexName.equals(that.classIndexName)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = classId.hashCode();
        result = 31 * result + classIndexName.hashCode();
        return result;
    }
}
