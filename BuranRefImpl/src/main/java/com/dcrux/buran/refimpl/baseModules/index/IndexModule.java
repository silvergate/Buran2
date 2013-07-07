package com.dcrux.buran.refimpl.baseModules.index;

import com.dcrux.buran.refimpl.baseModules.BaseModule;
import com.dcrux.buran.refimpl.baseModules.changeTracker.IChangeTracker;
import com.dcrux.buran.refimpl.baseModules.common.Module;
import com.dcrux.buran.refimpl.baseModules.label.RelationWrapper;
import com.dcrux.buran.refimpl.baseModules.nodeWrapper.LiveNode;

/**
 * Buran.
 *
 * @author: ${USER} Date: 04.07.13 Time: 14:33
 */
public class IndexModule extends Module<BaseModule> {
    public IndexModule(BaseModule baseModule) {
        super(baseModule);
    }

    public IChangeTracker createChangeTracker() {
        return new IChangeTracker() {
            @Override
            public void newNode(LiveNode newNode) {
                System.out.println("  CHANGE TRACKER: New Node");
            }

            @Override
            public void updatedNode(LiveNode updatedNode) {
                System.out.println("  CHANGE TRACKER: Updated Node");
            }

            @Override
            public void removedNode(LiveNode deletedNode) {
                System.out.println("  CHANGE TRACKER: Removed Node");
            }

            @Override
            public void newRelation(LiveNode sourceNode, RelationWrapper relation) {
                System.out.println("  CHANGE TRACKER: New Relation: " +
                        relation.getDocument().getIdentity().toString());
            }

            @Override
            public void removedRelation(LiveNode sourceNode, RelationWrapper relation) {
                System.out.println("  CHANGE TRACKER: Removed relation");
            }
        };
    }
}
