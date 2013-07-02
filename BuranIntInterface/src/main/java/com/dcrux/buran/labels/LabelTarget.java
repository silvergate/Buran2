package com.dcrux.buran.labels;

import com.dcrux.buran.INid;
import com.dcrux.buran.Version;
import com.google.common.base.Optional;

/**
 * Buran.
 *
 * @author: ${USER} Date: 01.07.13 Time: 01:44
 */
public class LabelTarget implements ILabelTargetInc, ILabelTarget {
    private final INid targetNid;
    private final Optional<Version> version;

    public LabelTarget(INid targetNid, Optional<Version> version) {
        this.targetNid = targetNid;
        this.version = version;
    }
}
