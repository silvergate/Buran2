package com.dcrux.buran.common.indexing.mapInput;

import com.dcrux.buran.scripting.iface.VarName;

import java.util.HashMap;
import java.util.Map;

/**
 * Buran.
 *
 * @author: ${USER} Date: 08.07.13 Time: 00:40
 */
public class NodeMapInput implements IMapInput {
    private Map<VarName, IFieldTarget> fields = new HashMap<VarName, IFieldTarget>();

    public Map<VarName, IFieldTarget> getFields() {
        return fields;
    }
}
