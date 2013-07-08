package com.dcrux.buran.refimpl.baseModules.changeTracker;

import com.dcrux.buran.common.fields.FieldIndex;
import com.dcrux.buran.refimpl.baseModules.edge.RelationWrapper;
import com.dcrux.buran.refimpl.baseModules.nodeWrapper.CommonNode;
import com.dcrux.buran.refimpl.baseModules.nodeWrapper.LiveNode;

/**
 * Buran.
 *
 * @author: ${USER} Date: 08.07.13 Time: 10:40
 */
public class NullChangeTracker implements IChangeTracker {

    public static final NullChangeTracker SINGLETON = new NullChangeTracker();

    @Override
    public void newNode(LiveNode newNode) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void updatedNode(LiveNode updatedNode) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void removedNode(LiveNode deletedNode) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void newRelation(LiveNode sourceNode, RelationWrapper relation) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void removedRelation(LiveNode sourceNode, RelationWrapper relation) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void fieldUpdate(Time time, FieldIndex fieldIndex, CommonNode sourceNode) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
