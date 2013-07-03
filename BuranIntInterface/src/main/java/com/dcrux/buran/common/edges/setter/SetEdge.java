package com.dcrux.buran.common.edges.setter;

import com.dcrux.buran.common.edges.IEdgeSetter;
import com.dcrux.buran.common.edges.IEdgeTargetInc;
import com.dcrux.buran.common.edges.ILabelName;
import com.dcrux.buran.common.edges.LabelIndex;

import java.util.HashMap;
import java.util.Map;

/**
 * Buran.
 *
 * @author: ${USER} Date: 01.07.13 Time: 01:52
 */
public class SetEdge implements IEdgeSetter {
    private final ILabelName name;
    private final Map<LabelIndex, IEdgeTargetInc> targets = new HashMap<>();

    public SetEdge(ILabelName name) {
        this.name = name;
    }

    public static SetEdge c(ILabelName name) {
        return new SetEdge(name);
    }

    public SetEdge add(LabelIndex index, IEdgeTargetInc target) {
        this.targets.put(index, target);
        return this;
    }

    public ILabelName getName() {
        return name;
    }

    public Map<LabelIndex, IEdgeTargetInc> getTargets() {
        return targets;
    }
}
