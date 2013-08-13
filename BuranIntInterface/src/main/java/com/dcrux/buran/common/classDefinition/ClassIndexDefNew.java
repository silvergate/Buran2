package com.dcrux.buran.common.classDefinition;

import com.dcrux.buran.common.query.SingleIndexDef;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Buran.
 *
 * @author: ${USER} Date: 06.08.13 Time: 23:00
 */
public class ClassIndexDefNew implements Serializable {
    private final Map<ClassIndexId, SingleIndexDef> indexes =
            new HashMap<ClassIndexId, SingleIndexDef>();


    public Map<ClassIndexId, SingleIndexDef> getIndexes() {
        return indexes;
    }

    public void add(SingleIndexDef singleIndexDef) {
        getIndexes().put(ClassIndexId.DEFAULT, singleIndexDef);
    }

    public SingleIndexDef add() {
        final SingleIndexDef singleIndexDef = new SingleIndexDef();
        getIndexes().put(ClassIndexId.DEFAULT, singleIndexDef);
        return singleIndexDef;
    }
}
