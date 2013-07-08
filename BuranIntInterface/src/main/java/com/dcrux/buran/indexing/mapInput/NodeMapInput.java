package com.dcrux.buran.indexing.mapInput;

import com.dcrux.buran.scripting.iface.VarName;

import java.util.HashMap;
import java.util.Map;

/**
 * Buran.
 *
 * @author: ${USER} Date: 08.07.13 Time: 00:40
 */
public class NodeMapInput implements IMapInput {
    private final Map<VarName, IFieldTarget> fields = new HashMap<>();

    public Map<VarName, IFieldTarget> getFields() {
        return fields;
    }
}
