package com.dcrux.buran.common.nodes.setter;

import com.dcrux.buran.common.classes.ClassId;
import com.dcrux.buran.common.nodes.INodeSetter;

/**
 * Buran.
 *
 * @author: ${USER} Date: 10.07.13 Time: 19:09
 */
public class ClassIdMut implements INodeSetter {
    public static enum Type {
        add,
        remove
    }

    private Type type;
    private ClassId classId;

    public static ClassIdMut add(ClassId classId) {
        return new ClassIdMut(Type.add, classId);
    }

    public static ClassIdMut remove(ClassId classId) {
        return new ClassIdMut(Type.remove, classId);
    }

    private ClassIdMut() {
    }

    public ClassIdMut(Type type, ClassId classId) {
        this.type = type;
        this.classId = classId;
    }

    public Type getType() {
        return type;
    }

    public ClassId getClassId() {
        return classId;
    }
}
