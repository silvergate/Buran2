package com.dcrux.buran.common.edges.getter;

import com.dcrux.buran.common.edges.IEdgeTarget;
import com.dcrux.buran.common.edges.LabelIndex;
import com.google.common.collect.Multimap;

import java.io.Serializable;

/**
 * Buran.
 *
 * @author: ${USER} Date: 02.07.13 Time: 02:45
 */
public class GetEdgeResult implements Serializable {

    //TODO: Sollen sortiert sein? (Vermutlich ja, sind ja auch so in der DB)
    private Multimap<LabelIndex, IEdgeTarget> entries;

    public GetEdgeResult(Multimap<LabelIndex, IEdgeTarget> entries) {
        this.entries = entries;
    }

    private GetEdgeResult() {
    }

    public Multimap<LabelIndex, IEdgeTarget> getEntries() {
        return entries;
    }

    @Override
    public String toString() {
        return "GetEdgeResult{" +
                "entries=" + entries +
                '}';
    }
}
