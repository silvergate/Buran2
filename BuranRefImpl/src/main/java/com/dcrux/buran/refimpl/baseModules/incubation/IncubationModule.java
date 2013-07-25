package com.dcrux.buran.refimpl.baseModules.incubation;

import com.dcrux.buran.common.IncNid;
import com.dcrux.buran.common.UserId;
import com.dcrux.buran.common.classes.ClassId;
import com.dcrux.buran.common.exceptions.IncNodeNotFound;
import com.dcrux.buran.common.exceptions.NodeClassNotFoundException;
import com.dcrux.buran.common.exceptions.NodeNotFoundException;
import com.dcrux.buran.refimpl.baseModules.BaseModule;
import com.dcrux.buran.refimpl.baseModules.common.*;
import com.dcrux.buran.refimpl.baseModules.nodeWrapper.IncubationNode;
import com.dcrux.buran.refimpl.baseModules.nodeWrapper.LiveNode;
import com.google.common.base.Optional;
import com.orientechnologies.orient.core.record.impl.ODocument;

/**
 * Buran.
 *
 * @author: ${USER} Date: 29.06.13 Time: 13:59
 */
public class IncubationModule extends Module<BaseModule> {

    public IncubationModule(BaseModule baseModule) {
        super(baseModule);
    }

    public OIncNid createIncNt(final UserId sender, final ClassId classId)
            throws NodeClassNotFoundException {
        getBase().getClassesModule().assureOrientClass(classId);
        final IncubationNode incubationNode = IncubationNode
                .createNew(classId, getBase().getCurrentTimestampProvider().get(), sender);
        incubationNode.getDocument().save();
        return incubationNode.getIncNid();
    }

    ;

    public OIncNid createIncUpdate(final UserId sender, ONidVer nidVerToUpdate)
            throws NodeNotFoundException {
        final LiveNode nodeToUpdate = getBase().getDataFetchModule().getNode(nidVerToUpdate);

        final IncubationNode incubationNode = IncubationNode.createUpdate(nodeToUpdate.getClassId(),
                getBase().getCurrentTimestampProvider().get(), sender, nidVerToUpdate);
        incubationNode.getDocument().save();
        return incubationNode.getIncNid();
    }

    ;

    public IncubationNode getIncNodeReq(final UserId sender, IncNid nid) throws IncNodeNotFound {
        final Optional<IncubationNode> incNode = getIncNode(sender, nid);
        if (!incNode.isPresent()) {
            throw new IncNodeNotFound();
        }
        return incNode.get();
    }

    public Optional<IncubationNode> getIncNode(final UserId sender, IncNid nid) {
        final ONid oIncNid = IfaceUtils.getOincNid(nid);
        final ODocument doc = getBase().getDb().load(oIncNid.getRecordId());
        final IncubationNode incubationNode = new IncubationNode(doc);
        if (!sender.equals(incubationNode.getSender())) {
                    /* Wrong sender, nothing found */
            return Optional.absent();
        }
        if (incubationNode.isLive()) {
                    /* Not incubation node */
            return Optional.absent();
        }

        return Optional.of(incubationNode);
    }

}
