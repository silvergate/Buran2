package com.dcrux.buran.refimpl.baseModules.newIndexing;

import com.dcrux.buran.common.Nid;
import com.dcrux.buran.common.exceptions.NodeNotFoundException;
import com.dcrux.buran.refimpl.baseModules.BaseModule;
import com.dcrux.buran.refimpl.baseModules.common.ONidVer;
import com.dcrux.buran.refimpl.baseModules.nodeWrapper.CommonNode;
import com.orientechnologies.orient.core.db.record.OIdentifiable;
import com.orientechnologies.orient.core.id.ORID;
import com.sun.istack.internal.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * Buran.
 *
 * @author: ${USER} Date: 06.08.13 Time: 23:45
 */
public class NodeCache {

    private final Map<OIdentifiable, CommonNode> nodeMapVer =
            new HashMap<OIdentifiable, CommonNode>();
    private final Map<OIdentifiable, CommonNode> nodeMap = new HashMap<OIdentifiable, CommonNode>();

    @Nullable
    public CommonNode getNodeVer(OIdentifiable versionsRecord) {
        return this.nodeMapVer.get(versionsRecord);
    }

    @Nullable
    public CommonNode getNode(OIdentifiable nodeId) {
        return this.nodeMap.get(nodeId);
    }

    private void addToCache(BaseModule baseModule, CommonNode node) throws NodeNotFoundException {
        ONidVer onidVer =
                baseModule.getDataFetchModule().toNidVer(new Nid(node.getNid().getAsString()));
        this.nodeMapVer.put(onidVer.getoIdentifiable(), node);
        this.nodeMap.put(node.getOrid(), node);
    }

    public CommonNode getNodeVerLoad(BaseModule baseModule, ORID versionsRecord)
            throws NodeNotFoundException {
        CommonNode node = getNodeVer(versionsRecord);
        if (node == null) {
            node = baseModule.getDataFetchModule().getNode(versionsRecord);
            addToCache(baseModule, node);
            return node;
        } else {
            return node;
        }
    }

    public CommonNode getNodeLoad(BaseModule baseModule, ORID nodeId) throws NodeNotFoundException {
        CommonNode node = getNode(nodeId);
        if (node == null) {
            node = baseModule.getDataFetchModule().getNode(new Nid(nodeId.toString()));
            addToCache(baseModule, node);
            return node;
        } else {
            return node;
        }
    }


}
