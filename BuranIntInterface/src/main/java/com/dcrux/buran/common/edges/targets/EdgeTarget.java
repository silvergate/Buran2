package com.dcrux.buran.common.edges.targets;

import com.dcrux.buran.common.INidOrNidVer;
import com.dcrux.buran.common.Version;
import com.dcrux.buran.common.edges.IEdgeTarget;
import com.dcrux.buran.common.edges.IEdgeTargetInc;
import com.google.common.base.Optional;

/**
 * Buran.
 *
 * @author: ${USER} Date: 01.07.13 Time: 01:44
 */
public class EdgeTarget implements IEdgeTargetInc, IEdgeTarget {
    private INidOrNidVer targetNid;
    private Optional<Version> version;

    public EdgeTarget(INidOrNidVer targetNid, Optional<Version> version) {
        this.targetNid = targetNid;
        this.version = version;
    }

    public static EdgeTarget unversioned(INidOrNidVer targetNid) {
        return new EdgeTarget(targetNid, Optional.<Version>absent());
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

    public Optional<Version> getVersion() {
        return version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EdgeTarget that = (EdgeTarget) o;

        if (!targetNid.equals(that.targetNid)) return false;
        if (!version.equals(that.version)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = targetNid.hashCode();
        result = 31 * result + version.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "EdgeTarget{" +
                "targetNid=" + targetNid +
                ", version=" + version +
                '}';
    }
}
