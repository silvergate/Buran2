package com.dcrux.buran.refimpl.modules.classes;

import com.dcrux.buran.common.classDefinition.ClassDefinition;
import com.dcrux.buran.common.classes.ClassId;
import com.dcrux.buran.common.exceptions.NodeClassNotFoundException;
import com.dcrux.buran.refimpl.modules.BaseModule;

import java.util.HashMap;
import java.util.Map;

/**
 * Buran.
 *
 * @author: ${USER} Date: 07.08.13 Time: 01:23
 */
public class ClassDefCache {
    private final Map<ClassId, ClassDefExt> classIdClassDefExtMap = new HashMap<>();

    public ClassDefExt getClassDefExt(BaseModule baseModule, ClassId classId)
            throws NodeClassNotFoundException {
        ClassDefExt classDefExt = this.classIdClassDefExtMap.get(classId);
        if (classDefExt == null) {
            classDefExt = baseModule.getClassesModule().getClassDefExtById(classId);
            this.classIdClassDefExtMap.put(classId, classDefExt);
        }
        return classDefExt;
    }

    public ClassDefinition getClassDef(BaseModule baseModule, ClassId classId)
            throws NodeClassNotFoundException {
        ClassDefExt classDefExt = getClassDefExt(baseModule, classId);
        return classDefExt.getClassDefinition();
    }
}
