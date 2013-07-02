package com.dcrux.buran.common.labels.targets;

import com.dcrux.buran.common.INid;
import com.dcrux.buran.common.UserId;
import com.dcrux.buran.common.Version;
import com.dcrux.buran.common.labels.ILabelTarget;
import com.dcrux.buran.common.labels.ILabelTargetInc;
import com.google.common.base.Optional;

/**
 * Buran.
 *
 * @author: ${USER} Date: 01.07.13 Time: 01:44
 */
public class LabelTargetExt implements ILabelTargetInc, ILabelTarget {
    private final UserId userId;
    private final INid targetNid;
    private final Optional<Version> version;

    public LabelTargetExt(UserId userId, INid targetNid, Optional<Version> version) {
        this.userId = userId;
        this.targetNid = targetNid;
        this.version = version;
    }

    public UserId getUserId() {
        return userId;
    }

    public INid getTargetNid() {
        return targetNid;
    }

    public Optional<Version> getVersion() {
        return version;
    }

    @Override
    public boolean isTargetIncubation() {
        return false;
    }

    @Override
    public String toString() {
        return "LabelTargetExt{" +
                "userId=" + userId +
                ", targetNid=" + targetNid +
                ", version=" + version +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LabelTargetExt that = (LabelTargetExt) o;

        if (!targetNid.equals(that.targetNid)) return false;
        if (!userId.equals(that.userId)) return false;
        if (!version.equals(that.version)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = userId.hashCode();
        result = 31 * result + targetNid.hashCode();
        result = 31 * result + version.hashCode();
        return result;
    }
}
