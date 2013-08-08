package com.dcrux.buran.common.fields.setter;

import com.dcrux.buran.common.classes.ClassId;
import com.dcrux.buran.common.fields.IFieldSetter;

/**
 * Buran.
 *
 * @author: ${USER} Date: 29.06.13 Time: 16:56
 */
public class FieldRemoveAll implements IFieldSetter {

    private ClassId classId;

    public FieldRemoveAll(ClassId classId) {
        this.classId = classId;
    }

    public static FieldRemoveAll c(ClassId classId) {
        return new FieldRemoveAll(classId);
    }

    public ClassId getClassId() {
        return classId;
    }

    private FieldRemoveAll() {
    }
}
