package com.dcrux.buran.labels;

import com.dcrux.buran.IIncNid;
import com.dcrux.buran.Version;
import com.google.common.base.Optional;

/**
 * Buran.
 *
 * @author: ${USER} Date: 01.07.13 Time: 01:44
 */
public class LabelTargetInc implements ILabelTargetInc, ILabelTarget {
    private final IIncNid targetNid;
    private final Optional<Version> version;

    public LabelTargetInc(IIncNid targetNid, Optional<Version> version) {
        this.targetNid = targetNid;
        this.version = version;
    }

    public static LabelTargetInc unversioned(IIncNid target) {
        return new LabelTargetInc(target, Optional.<Version>absent());
    }

    public IIncNid getTargetNid() {
        return targetNid;
    }

    public Optional<Version> getVersion() {
        return version;
    }
}
