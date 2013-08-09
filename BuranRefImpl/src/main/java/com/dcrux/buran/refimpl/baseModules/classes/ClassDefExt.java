package com.dcrux.buran.refimpl.baseModules.classes;

import com.dcrux.buran.common.classDefinition.ClassDefinition;
import com.dcrux.buran.common.classDefinition.DependencyIndex;
import com.dcrux.buran.common.classes.ClassId;

import java.io.Serializable;
import java.util.Map;

/**
 * Buran.
 *
 * @author: ${USER} Date: 08.07.13 Time: 22:34
 */
public class ClassDefExt implements Serializable {
    public ClassDefExt(ClassDefinition classDefinition,
            Map<DependencyIndex, ClassId> dependencies) {
        this.classDefinition = classDefinition;
        this.dependencies = dependencies;
    }

    private final ClassDefinition classDefinition;
    private final Map<DependencyIndex, ClassId> dependencies;

    public ClassDefinition getClassDefinition() {
        return classDefinition;
    }

    public Map<DependencyIndex, ClassId> getDependencies() {
        return dependencies;
    }
}
