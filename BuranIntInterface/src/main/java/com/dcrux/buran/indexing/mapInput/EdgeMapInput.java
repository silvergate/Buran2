package com.dcrux.buran.indexing.mapInput;

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
    private final Map<VarName, IFieldTarget> fields = new HashMap<>();
    private final ClassLabelName classLabelName;

    public EdgeMapInput(ClassLabelName classLabelName) {
        this.classLabelName = classLabelName;
    }

    public ClassLabelName getClassLabelName() {
        return classLabelName;
    }

    public Map<VarName, IFieldTarget> getFields() {
        return fields;
    }
}
