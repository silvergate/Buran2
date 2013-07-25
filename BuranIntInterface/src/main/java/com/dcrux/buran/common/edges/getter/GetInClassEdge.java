package com.dcrux.buran.common.edges.getter;

import com.dcrux.buran.common.INidOrNidVer;
import com.dcrux.buran.common.classes.ClassId;
import com.dcrux.buran.common.edges.ClassLabelName;
import com.dcrux.buran.common.edges.IEdgeGetter;
import com.dcrux.buran.common.edges.ILabelName;
import com.dcrux.buran.common.edges.LabelIndex;
import com.google.common.base.Optional;

/**
 * Buran.
 *
 * @author: ${USER} Date: 02.07.13 Time: 02:32
 */
public class GetInClassEdge implements IEdgeGetter<GetInClassEdgeResult> {
    private Optional<INidOrNidVer> sourceNode;
    private Optional<ClassId> sourceClassId;
    private ILabelName labelName;
    private LabelIndex fromIndex;
    private LabelIndex toIndex;
    private boolean versioned;

    public GetInClassEdge(Optional<INidOrNidVer> sourceNode, Optional<ClassId> sourceClassId,
            ILabelName labelName, LabelIndex fromIndex, LabelIndex toIndex, boolean versioned) {
        if (fromIndex.getIndex() > toIndex.getIndex()) {
            throw new IllegalArgumentException("fromIndex.getIndex()>toIndex.getIndex()");
        }
        this.sourceClassId = sourceClassId;
        if (sourceNode.isPresent() && sourceClassId.isPresent()) {
            throw new IllegalArgumentException(
                    "If souce node is given, " + "source class must be absent.");
        }
        if ((!(sourceClassId.isPresent() || sourceNode.isPresent())) &&
                (labelName instanceof ClassLabelName)) {
            throw new IllegalArgumentException(
                    "If a ClassLabelName is given, " + "source class or source node is mandatory");
        }
        this.sourceNode = sourceNode;
        this.labelName = labelName;
        this.fromIndex = fromIndex;
        this.toIndex = toIndex;
        this.versioned = versioned;
    }

    public static GetInClassEdge classEdge(ClassId sourceClassId, ILabelName labelName,
            LabelIndex index, boolean versioned) {
        return new GetInClassEdge(Optional.<INidOrNidVer>absent(), Optional.of(sourceClassId),
                labelName, index, index, versioned);
    }

    private GetInClassEdge() {
    }

    public Optional<ClassId> getSourceClassId() {
        return sourceClassId;
    }

    public ILabelName getLabelName() {
        return labelName;
    }

    public boolean isVersioned() {
        return versioned;
    }

    public LabelIndex getFromIndex() {
        return fromIndex;
    }

    public LabelIndex getToIndex() {
        return toIndex;
    }

    public Optional<INidOrNidVer> getSourceNode() {
        return sourceNode;
    }
}
