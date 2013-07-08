package com.dcrux.buran.refimpl.baseModules.changeTracker;

import com.dcrux.buran.common.fields.FieldIndex;
import com.dcrux.buran.refimpl.baseModules.edge.RelationWrapper;
import com.dcrux.buran.refimpl.baseModules.nodeWrapper.CommonNode;
import com.dcrux.buran.refimpl.baseModules.nodeWrapper.LiveNode;

/**
 * Buran.
 *
 * @author: ${USER} Date: 04.07.13 Time: 14:22
 */
@Deprecated
public interface IChangeTracker {
    void newNode(LiveNode newNode);

    void updatedNode(LiveNode updatedNode);

    void removedNode(LiveNode deletedNode);

    void newRelation(LiveNode sourceNode, RelationWrapper relation);

    void removedRelation(LiveNode sourceNode, RelationWrapper relation);

    void fieldUpdate(Time time, FieldIndex fieldIndex, CommonNode sourceNode);
}
