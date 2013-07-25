package com.dcrux.buran.refimpl.baseModules.edge;

import com.dcrux.buran.common.*;
import com.dcrux.buran.common.edges.IEdgeTarget;
import com.dcrux.buran.common.edges.targets.EdgeTarget;
import com.dcrux.buran.common.edges.targets.EdgeTargetExt;

/**
 * Buran.
 *
 * @author: ${USER} Date: 03.07.13 Time: 01:23
 */
public class RelWrapToLabelTarget {
    public static IEdgeTarget convert(RelationWrapper wrapper) {
        if (wrapper.getTargetUserId() != null) {
           /* External */
            final RelationWrapper.TargetType2 type = wrapper.getTargetType();
            IExtNidOrNidVer nid;
            switch (type) {
                case extUnversioned:
                    nid = new ExtNid(wrapper.getTargetUserId(),
                            new Nid(wrapper.getTarget().getAsString()));
                    break;
                case extVersioned:
                    nid = new ExtNidVer(wrapper.getTargetUserId(),
                            new NidVer(wrapper.getTarget().getAsString()));
                    break;
                default:
                    throw new IllegalArgumentException("Wrong target type");
            }

            return new EdgeTargetExt(wrapper.getTargetClass(), nid);
        } else {
            final RelationWrapper.TargetType2 type = wrapper.getTargetType();
            INidOrNidVer nid;
            switch (type) {
                case unversioned:
                    nid = new Nid(wrapper.getTarget().getAsString());
                    break;
                case versioned:
                    nid = new NidVer(wrapper.getTarget().getAsString());
                    break;
                default:
                    throw new IllegalArgumentException("Wrong target type");
            }
            return new EdgeTarget(nid);
        }
    }
}
