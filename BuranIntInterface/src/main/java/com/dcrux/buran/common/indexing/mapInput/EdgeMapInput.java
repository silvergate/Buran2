package com.dcrux.buran.common.indexing.mapInput;

import com.dcrux.buran.common.edges.ClassLabelName;
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
    private ClassLabelName classLabelName;

    public EdgeMapInput(ClassLabelName classLabelName) {
        this.classLabelName = classLabelName;
    }

    private EdgeMapInput() {
    }

    public ClassLabelName getClassLabelName() {
        return classLabelName;
    }

    public Map<VarName, IFieldTarget> getFields() {
        return fields;
    }
}
