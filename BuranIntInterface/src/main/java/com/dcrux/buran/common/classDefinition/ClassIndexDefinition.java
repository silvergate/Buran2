package com.dcrux.buran.common.classDefinition;

import com.dcrux.buran.common.indexing.IndexDefinition;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Buran.
 *
 * @author: ${USER} Date: 08.07.13 Time: 10:08
 */
public class ClassIndexDefinition implements Serializable {
    private final Map<ClassIndexName, IndexDefinition> indexDefinitionMap =
            new HashMap<ClassIndexName, IndexDefinition>();

    public ClassIndexDefinition add(ClassIndexName name, IndexDefinition definition) {
        this.indexDefinitionMap.put(name, definition);
        return this;
    }

    public Map<ClassIndexName, IndexDefinition> getIndexDefinitionMap() {
        return indexDefinitionMap;
    }
}
