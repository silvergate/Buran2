package com.dcrux.buran.common.edges.targets;

import com.dcrux.buran.common.IncNid;
import com.dcrux.buran.common.edges.IEdgeTargetInc;

/**
 * Buran.
 *
 * @author: ${USER} Date: 01.07.13 Time: 01:44
 */
public class EdgeTargetInc implements IEdgeTargetInc {
    private IncNid targetNid;
    private boolean versioned;

    public EdgeTargetInc(IncNid targetNid, boolean versioned) {
        this.targetNid = targetNid;
        this.versioned = versioned;
    }

    private EdgeTargetInc() {
    }

    public static EdgeTargetInc unversioned(IncNid target) {
        return new EdgeTargetInc(target, false);
    }

    public static EdgeTargetInc versioned(IncNid target) {
        return new EdgeTargetInc(target, true);
    }

    public IncNid getTargetNid() {
        return targetNid;
    }

    public boolean isVersioned() {
        return versioned;
    }

    @Override
    public boolean isTargetIncubation() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EdgeTargetInc that = (EdgeTargetInc) o;

        if (versioned != that.versioned) return false;
        if (!targetNid.equals(that.targetNid)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = targetNid.hashCode();
        result = 31 * result + (versioned ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "EdgeTargetInc{" +
                "targetNid=" + targetNid +
                ", versioned=" + versioned +
                '}';
    }
}
