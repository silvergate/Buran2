package com.dcrux.buran.refimpl.baseModules.index.functionCompiler;

import com.dcrux.buran.common.classDefinition.ClassIndexName;
import com.dcrux.buran.scripting.iface.compiler.CompiledBlock;

import java.io.Serializable;
import java.util.Map;

/**
 * Buran.
 *
 * @author: ${USER} Date: 09.07.13 Time: 16:02
 */
public class IndexingAdditionalInfo implements Serializable {
    private final Map<ClassIndexName, CompiledBlock> compiledIndexes;

    public IndexingAdditionalInfo(Map<ClassIndexName, CompiledBlock> compiledIndexes) {
        this.compiledIndexes = compiledIndexes;
    }

    public Map<ClassIndexName, CompiledBlock> getCompiledIndexes() {
        return compiledIndexes;
    }
}
