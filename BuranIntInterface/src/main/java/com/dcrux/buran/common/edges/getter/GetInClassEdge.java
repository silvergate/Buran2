package com.dcrux.buran.common.edges.getter;

import com.dcrux.buran.common.classes.ClassId;
import com.dcrux.buran.common.edges.ClassLabelName;
import com.dcrux.buran.common.edges.IEdgeGetter;
import com.dcrux.buran.common.edges.LabelIndex;

/**
 * Buran.
 *
 * @author: ${USER} Date: 02.07.13 Time: 02:32
 */
public class GetInClassEdge implements IEdgeGetter<GetInClassEdgeResult> {
    private ClassId classId;
    //TODO: Ein limit einbauen
    //TODO: Target version berücksichtigen!
    private ClassLabelName labelName;
    private LabelIndex fromIndex;
    private LabelIndex toIndex;

    public GetInClassEdge(ClassId classId, ClassLabelName labelName, LabelIndex fromIndex,
            LabelIndex toIndex) {
        if (fromIndex.getIndex() > toIndex.getIndex()) {
            throw new IllegalArgumentException("fromIndex.getIndex()>toIndex.getIndex()");
        }
        this.classId = classId;
        this.labelName = labelName;
        this.fromIndex = fromIndex;
        this.toIndex = toIndex;
    }

    private GetInClassEdge() {
    }

    public static GetInClassEdge c(ClassId classId, ClassLabelName labelName, LabelIndex fromIndex,
            LabelIndex toIndex) {
        return new GetInClassEdge(classId, labelName, fromIndex, toIndex);
    }

    public static GetInClassEdge c(ClassId classId, ClassLabelName labelName, LabelIndex index) {
        return new GetInClassEdge(classId, labelName, index, index);
    }

    public ClassLabelName getLabelName() {
        return labelName;
    }

    public LabelIndex getFromIndex() {
        return fromIndex;
    }

    public LabelIndex getToIndex() {
        return toIndex;
    }
}
