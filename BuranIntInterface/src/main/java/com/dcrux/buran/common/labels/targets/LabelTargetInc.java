package com.dcrux.buran.common.labels.targets;

import com.dcrux.buran.common.IIncNid;
import com.dcrux.buran.common.labels.ILabelTargetInc;

/**
 * Buran.
 *
 * @author: ${USER} Date: 01.07.13 Time: 01:44
 */
public class LabelTargetInc implements ILabelTargetInc {
    private final IIncNid targetNid;
    private final boolean versioned;

    public LabelTargetInc(IIncNid targetNid, boolean versioned) {
        this.targetNid = targetNid;
        this.versioned = versioned;
    }

    public static LabelTargetInc unversioned(IIncNid target) {
        return new LabelTargetInc(target, false);
    }

    public static LabelTargetInc versioned(IIncNid target) {
        return new LabelTargetInc(target, true);
    }

    public IIncNid getTargetNid() {
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

        LabelTargetInc that = (LabelTargetInc) o;

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
        return "LabelTargetInc{" +
                "targetNid=" + targetNid +
                ", versioned=" + versioned +
                '}';
    }
}
