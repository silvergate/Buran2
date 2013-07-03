package com.dcrux.buran.refimpl.baseModules.commit;

import com.dcrux.buran.common.*;
import com.dcrux.buran.common.exceptions.NodeNotFoundException;
import com.dcrux.buran.refimpl.baseModules.BaseModule;
import com.dcrux.buran.refimpl.baseModules.common.Module;
import com.dcrux.buran.refimpl.baseModules.common.ONid;
import com.dcrux.buran.refimpl.baseModules.nodeWrapper.IncubationNode;
import com.dcrux.buran.refimpl.baseModules.nodeWrapper.LiveNode;
import com.google.common.base.Optional;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Buran.
 *
 * @author: ${USER} Date: 02.07.13 Time: 13:33
 */
public class CommitModule extends Module<BaseModule> {
    public CommitModule(BaseModule baseModule) {
        super(baseModule);
    }

    private LiveNode commitNode(final IncubationNode incubationNode) throws NodeNotFoundException {
        final boolean update = incubationNode.getUpdateVersion() != null;
        if (!update) {
            incubationNode.goLive();
            final LiveNode storeNode = new LiveNode(incubationNode.getDocument());
            storeNode.setVersion(Version.INITIAL);
            incubationNode.getDocument().save();
            return storeNode;
        } else {
            final ONid upOnid = incubationNode.getUpdateNid();
            final Version upVersion = incubationNode.getUpdateVersion();
            LiveNode up = getBase().getDataFetchModule().getNodeReq(new NidVer(upOnid, upVersion));

                    /* Update node */
            if (up.getClassId().getId() != incubationNode.getClassId().getId()) {
                throw new IllegalStateException("Incompatible class");
            }
                    /* Remove fields */
            for (final String upFieldName : up.getDocument().fieldNames()) {
                up.getDocument().removeField(upFieldName);
            }
            for (final String fieldName : incubationNode.getDocument().fieldNames()) {
                final Object value = incubationNode.getDocument().field(fieldName);
                System.out.println("Transferring " + fieldName + ", value = " + value);
                up.getDocument().field(fieldName, value);
            }

            up.incVersion();
            up.getDocument().save();

            return up;
        }
    }

    public Map<IIncNid, NidVer> commit(final UserId sender, final Collection<IIncNid> incNids)
            throws IncNodeNotFound, NodeNotFoundException {
        final Map<IIncNid, NidVer> result = new HashMap<>();
        final CommitInfo commitInfo = new CommitInfo();

                /* Commit node */
        for (final IIncNid incNid : incNids) {
            final Optional<IncubationNode> iNode =
                    getBase().getIncubationModule().getIncNode(sender, incNid);
            if (!iNode.isPresent()) {
                throw new IncNodeNotFound("Inc node not found");
            }
            final IncubationNode node = iNode.get();
            final LiveNode liveNode = commitNode(node);
            result.put(incNid, liveNode.getNidVer());
            commitInfo.add(new CommitInfo.CommitEntry(node, liveNode));
        }

                /* Commit label */
        for (CommitInfo.CommitEntry entry : commitInfo.getCommitEntrySet()) {
            getBase().getLabelModule()
                    .commit(entry.getIncubationNode(), entry.getLiveNode(), commitInfo);
        }

                /* Remove incubation */
        for (CommitInfo.CommitEntry entry : commitInfo.getCommitEntrySet()) {
            if (entry.isUpdate()) {
                removeIncNode(new ONid(entry.getIncubationNode().getNid().getRecordId()));
            }
        }

        return result;
    }

    private void removeIncNode(final ONid incNid) {
        // TODO: Alle felder und so zeugs muss auch weg.
        getBase().getDb().delete(incNid.getRecordId());
    }


}
