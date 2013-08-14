package com.dcrux.buran.refimpl.modules.nodeWrapper;

import com.dcrux.buran.common.classes.ClassId;
import com.dcrux.buran.common.fields.FieldIndex;

/**
 * Buran.
 *
 * @author: ${USER} Date: 09.08.13 Time: 12:17
 */
public class FieldIndexAndClassId {
    private FieldIndex index;
    private ClassId classId;
    private boolean primaryClass;

    public FieldIndexAndClassId(FieldIndex index, ClassId classId, boolean primaryClass) {
        this.index = index;
        this.classId = classId;
        this.primaryClass = primaryClass;
    }

    public FieldIndex getIndex() {
        return index;
    }

    public ClassId getClassId() {
        return classId;
    }

    public boolean isPrimaryClass() {
        return primaryClass;
    }
}
