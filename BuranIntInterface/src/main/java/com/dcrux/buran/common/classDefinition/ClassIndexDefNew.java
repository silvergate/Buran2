package com.dcrux.buran.common.classDefinition;

import com.dcrux.buran.queryDsl.SingleIndexDef;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Buran.
 *
 * @author: ${USER} Date: 06.08.13 Time: 23:00
 */
public class ClassIndexDefNew implements Serializable {
    private final Map<ClassIndexNameNew, SingleIndexDef> indexes =
            new HashMap<ClassIndexNameNew, SingleIndexDef>();


    public Map<ClassIndexNameNew, SingleIndexDef> getIndexes() {
        return indexes;
    }
}
