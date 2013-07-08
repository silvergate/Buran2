package com.dcrux.buran.refimpl.baseModules.edge;

import com.dcrux.buran.common.edges.IEdgeTarget;
import com.dcrux.buran.common.edges.targets.EdgeTarget;
import com.dcrux.buran.common.edges.targets.EdgeTargetExt;
import com.google.common.base.Optional;

/**
 * Buran.
 *
 * @author: ${USER} Date: 03.07.13 Time: 01:23
 */
public class RelWrapToLabelTarget {
    public static IEdgeTarget convert(RelationWrapper wrapper) {
        if (wrapper.getTargetUserId() != null) {
           /* External */
            return new EdgeTargetExt(wrapper.getTargetUserId(), wrapper.getTarget(),
                    Optional.fromNullable(wrapper.getTargetVersion()));
        }
        return new EdgeTarget(wrapper.getTarget(),
                Optional.fromNullable(wrapper.getTargetVersion()));
    }
}
