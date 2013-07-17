package com.dcrux.buran.refimpl.baseModules.classes;

import com.dcrux.buran.common.classDefinition.ClassDefinition;
import com.dcrux.buran.common.classDefinition.ClassIndexName;
import com.dcrux.buran.scripting.compiler.CompiledBlock;

import java.io.Serializable;
import java.util.Map;

/**
 * Buran.
 *
 * @author: ${USER} Date: 08.07.13 Time: 22:34
 */
public class ClassDefExt implements Serializable {
    public ClassDefExt(ClassDefinition classDefinition,
            Map<ClassIndexName, CompiledBlock> combiledMapFunctions) {
        this.classDefinition = classDefinition;
        this.combiledMapFunctions = combiledMapFunctions;
    }

    private final ClassDefinition classDefinition;
    private final Map<ClassIndexName, CompiledBlock> combiledMapFunctions;

    public ClassDefinition getClassDefinition() {
        return classDefinition;
    }

    public Map<ClassIndexName, CompiledBlock> getCombiledMapFunctions() {
        return combiledMapFunctions;
    }
}
