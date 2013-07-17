package com.dcrux.buran.common.edges.getter;

import com.dcrux.buran.common.edges.IEdgeGetter;
import com.dcrux.buran.common.edges.ILabelName;
import com.dcrux.buran.common.edges.LabelIndex;

/**
 * Buran.
 *
 * @author: ${USER} Date: 02.07.13 Time: 02:32
 */
public class GetEdge implements IEdgeGetter<GetEdgeResult> {
    private ILabelName labelName;
    private LabelIndex fromIndex;
    private LabelIndex toIndex;

    public GetEdge(ILabelName labelName, LabelIndex fromIndex, LabelIndex toIndex) {
        if (fromIndex.getIndex() > toIndex.getIndex()) {
            throw new IllegalArgumentException("fromIndex.getIndex()>toIndex.getIndex()");
        }
        this.labelName = labelName;
        this.fromIndex = fromIndex;
        this.toIndex = toIndex;
    }

    private GetEdge() {
    }

    public static GetEdge c(ILabelName labelName, LabelIndex fromIndex, LabelIndex toIndex) {
        return new GetEdge(labelName, fromIndex, toIndex);
    }

    public static GetEdge c(ILabelName labelName, LabelIndex index) {
        return new GetEdge(labelName, index, index);
    }

    public ILabelName getLabelName() {
        return labelName;
    }

    public LabelIndex getFromIndex() {
        return fromIndex;
    }

    public LabelIndex getToIndex() {
        return toIndex;
    }
}
