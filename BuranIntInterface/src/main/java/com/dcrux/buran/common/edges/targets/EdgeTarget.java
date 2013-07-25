package com.dcrux.buran.common.edges.targets;

import com.dcrux.buran.common.INidOrNidVer;
import com.dcrux.buran.common.Nid;
import com.dcrux.buran.common.NidVer;
import com.dcrux.buran.common.edges.IEdgeTarget;
import com.dcrux.buran.common.edges.IEdgeTargetInc;

/**
 * Buran.
 *
 * @author: ${USER} Date: 01.07.13 Time: 01:44
 */
public class EdgeTarget implements IEdgeTargetInc, IEdgeTarget {
    private INidOrNidVer targetNid;

    public EdgeTarget(INidOrNidVer targetNid) {
        this.targetNid = targetNid;
    }

    public static EdgeTarget unversioned(Nid targetNid) {
        return new EdgeTarget(targetNid);
    }

    public static EdgeTarget versioned(NidVer nidVer) {
        return new EdgeTarget(nidVer);
    }

    private EdgeTarget() {
    }

    @Override
    public boolean isTargetIncubation() {
        return false;
    }

    public INidOrNidVer getTargetNid() {
        return targetNid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EdgeTarget that = (EdgeTarget) o;

        if (!targetNid.equals(that.targetNid)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return targetNid.hashCode();
    }

    @Override
    public String toString() {
        return "EdgeTarget{" +
                "targetNid=" + targetNid +
                '}';
    }
}
