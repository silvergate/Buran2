package com.dcrux.buran.refimpl.baseModules.index.eval;

import com.dcrux.buran.common.edges.ILabelName;
import com.dcrux.buran.common.edges.LabelIndex;

import java.util.HashMap;
import java.util.Map;

/**
 * Buran.
 *
 * @author: ${USER} Date: 04.08.13 Time: 01:57
 */
public class EdgeBeans {
    private Map<ILabelName, Map<LabelIndex, EdgeBean>> edges =
            new HashMap<ILabelName, Map<LabelIndex, EdgeBean>>();

    public Map<ILabelName, Map<LabelIndex, EdgeBean>> getEdges() {
        return edges;
    }
}
