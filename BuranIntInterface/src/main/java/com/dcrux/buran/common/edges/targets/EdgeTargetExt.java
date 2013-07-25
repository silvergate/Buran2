package com.dcrux.buran.common.edges.targets;

import com.dcrux.buran.common.IExtNidOrNidVer;
import com.dcrux.buran.common.classes.ClassId;
import com.dcrux.buran.common.edges.IEdgeTarget;
import com.dcrux.buran.common.edges.IEdgeTargetInc;

/**
 * Buran.
 *
 * @author: ${USER} Date: 01.07.13 Time: 01:44
 */
public class EdgeTargetExt implements IEdgeTargetInc, IEdgeTarget {

    private IExtNidOrNidVer extNidOrNidVer;
    private ClassId targetClassId;

    public EdgeTargetExt(ClassId targetClassId, IExtNidOrNidVer extNidOrNidVer) {
        this.targetClassId = targetClassId;
        this.extNidOrNidVer = extNidOrNidVer;
    }

    private EdgeTargetExt() {
    }

    public IExtNidOrNidVer getExtNidOrNidVer() {
        return extNidOrNidVer;
    }

    public ClassId getTargetClassId() {
        return targetClassId;
    }

    @Override
    public boolean isTargetIncubation() {
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EdgeTargetExt that = (EdgeTargetExt) o;

        if (!extNidOrNidVer.equals(that.extNidOrNidVer)) return false;
        if (!targetClassId.equals(that.targetClassId)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = extNidOrNidVer.hashCode();
        result = 31 * result + targetClassId.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "EdgeTargetExt{" +
                "extNidOrNidVer=" + extNidOrNidVer +
                ", targetClassId=" + targetClassId +
                '}';
    }
}
