package com.dcrux.buran.common.labels.getter;

import com.dcrux.buran.common.labels.ILabelTarget;
import com.dcrux.buran.common.labels.LabelIndex;
import com.google.common.collect.Multimap;

/**
 * Buran.
 *
 * @author: ${USER} Date: 02.07.13 Time: 02:45
 */
public class GetLabelResult {

    //TODO: Sollen sortiert sein
    private final Multimap<LabelIndex, ILabelTarget> entries;

    public GetLabelResult(Multimap<LabelIndex, ILabelTarget> entries) {
        this.entries = entries;
    }

    public Multimap<LabelIndex, ILabelTarget> getEntries() {
        return entries;
    }

    @Override
    public String toString() {
        return "GetLabelResult{" +
                "entries=" + entries +
                '}';
    }
}
