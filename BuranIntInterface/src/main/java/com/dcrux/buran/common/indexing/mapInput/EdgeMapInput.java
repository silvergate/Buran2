package com.dcrux.buran.common.indexing.mapInput;

import com.dcrux.buran.common.edges.ClassLabelName;
import com.dcrux.buran.common.edges.ILabelName;
import com.dcrux.buran.scripting.iface.VarName;

import java.util.HashMap;
import java.util.Map;

/**
 * Buran.
 *
 * @author: ${USER} Date: 08.07.13 Time: 00:40
 */
public class EdgeMapInput implements IMapInput {
    private Map<VarName, IFieldTarget> fields = new HashMap<VarName, IFieldTarget>();
    private Map<VarName, EdgeFieldTarget> edgeFields = new HashMap<VarName, EdgeFieldTarget>();
    private ILabelName labelName;

    public EdgeMapInput(ClassLabelName labelName) {
        this.labelName = labelName;
    }

    private EdgeMapInput() {
    }

    public ILabelName getLabelName() {
        return labelName;
    }

    public Map<VarName, IFieldTarget> getFields() {
        return fields;
    }

    public Map<VarName, EdgeFieldTarget> getEdgeFields() {
        return edgeFields;
    }
}
