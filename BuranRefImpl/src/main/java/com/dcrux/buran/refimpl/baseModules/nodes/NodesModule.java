package com.dcrux.buran.refimpl.baseModules.nodes;

import com.dcrux.buran.common.nodes.INodeSetter;
import com.dcrux.buran.common.nodes.setter.NodeRemove;
import com.dcrux.buran.refimpl.baseModules.BaseModule;
import com.dcrux.buran.refimpl.baseModules.common.Module;
import com.dcrux.buran.refimpl.baseModules.nodeWrapper.CommonNode;
import com.dcrux.buran.refimpl.baseModules.nodeWrapper.LiveNode;

/**
 * Buran.
 *
 * @author: ${USER} Date: 10.07.13 Time: 19:17
 */
public class NodesModule extends Module<BaseModule> {
    public NodesModule(BaseModule baseModule) {
        super(baseModule);
    }

    public void performNodeSet(CommonNode node, INodeSetter nodeSetter) {
        if (nodeSetter instanceof NodeRemove) {
            final LiveNode liveNode = new LiveNode(node.getDocument());
            liveNode.markForDeletion();
        } else {
            throw new IllegalArgumentException("Unknown node setter");
        }
    }
}
