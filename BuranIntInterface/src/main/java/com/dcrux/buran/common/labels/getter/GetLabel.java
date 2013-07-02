package com.dcrux.buran.common.labels.getter;

import com.dcrux.buran.common.labels.ILabelGet;
import com.dcrux.buran.common.labels.ILabelName;
import com.dcrux.buran.common.labels.LabelIndex;

/**
 * Buran.
 *
 * @author: ${USER} Date: 02.07.13 Time: 02:32
 */
public class GetLabel implements ILabelGet<GetLabelResult> {
    private final ILabelName labelName;
    private final LabelIndex fromIndex;
    private final LabelIndex toIndex;

    public GetLabel(ILabelName labelName, LabelIndex fromIndex, LabelIndex toIndex) {
        this.labelName = labelName;
        this.fromIndex = fromIndex;
        this.toIndex = toIndex;
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
