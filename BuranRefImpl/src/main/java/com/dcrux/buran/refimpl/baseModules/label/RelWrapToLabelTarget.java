package com.dcrux.buran.refimpl.baseModules.label;

import com.dcrux.buran.common.labels.ILabelTarget;
import com.dcrux.buran.common.labels.targets.LabelTarget;
import com.dcrux.buran.common.labels.targets.LabelTargetExt;
import com.google.common.base.Optional;

/**
 * Buran.
 *
 * @author: ${USER} Date: 03.07.13 Time: 01:23
 */
public class RelWrapToLabelTarget {
    public static ILabelTarget convert(RelationWrapper wrapper) {
        if (wrapper.getTargetUserId() != null) {
           /* External */
            return new LabelTargetExt(wrapper.getTargetUserId(), wrapper.getTarget(),
                    Optional.fromNullable(wrapper.getTargetVersion()));
        }
        return new LabelTarget(wrapper.getTarget(),
                Optional.fromNullable(wrapper.getTargetVersion()));
    }
}
