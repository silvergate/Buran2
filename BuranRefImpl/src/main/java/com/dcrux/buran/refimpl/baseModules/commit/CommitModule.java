package com.dcrux.buran.refimpl.baseModules.commit;

import com.dcrux.buran.common.*;
import com.dcrux.buran.common.getterSetter.IDataSetter;
import com.dcrux.buran.refimpl.baseModules.BaseModule;
import com.dcrux.buran.refimpl.baseModules.common.Module;
import com.dcrux.buran.refimpl.baseModules.common.ONid;
import com.dcrux.buran.refimpl.baseModules.deltaRecorder.IRecordPlayer;
import com.dcrux.buran.refimpl.baseModules.nodeWrapper.IncubationNode;
import com.dcrux.buran.refimpl.baseModules.nodeWrapper.LiveNode;
import com.google.common.base.Optional;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.orientechnologies.orient.core.db.record.OIdentifiable;
import com.sun.istack.internal.Nullable;

import java.util.*;

/**
 * Buran.
 *
 * @author: ${USER} Date: 02.07.13 Time: 13:33
 */
public class CommitModule extends Module<BaseModule> {
    public CommitModule(BaseModule baseModule) {
        super(baseModule);
    }

    private void playRecordedDeltas(final UserId sender, final IncubationNode incubationNode,
            final LiveNode applyTo, @Nullable final Set<OIdentifiable> outCommittableRelations)
            throws Exception {
        getBase().getDeltaRecorderModule()
                .playRecords(incubationNode.getNid(), new IRecordPlayer() {

                    @Override
                    public void entry(ONid onid, IDataSetter setter) throws Exception {
                        getBase().getDataMutModule()
                                .setDataDirect(sender, applyTo, setter, outCommittableRelations);
                    }
                });
    }

    private LiveNode commitNode(final UserId sender, final IncubationNode incubationNode,
            @Nullable final Set<OIdentifiable> outCommittableRelations) throws Exception {
        final boolean update = incubationNode.isUpdate();
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

            playRecordedDeltas(sender, incubationNode, up, outCommittableRelations);

            up.incVersion();
            up.getDocument().save();
            return up;
        }
    }

    public Map<IIncNid, NidVer> commit(final UserId sender, final Collection<IIncNid> incNids)
            throws Exception {
        final Map<IIncNid, NidVer> result = new HashMap<>();
        final CommitInfo commitInfo = new CommitInfo();

        final Multimap<OIdentifiable, OIdentifiable> committableRelationsByNode =
                HashMultimap.create();

        /* Commit node */
        for (final IIncNid incNid : incNids) {
            final Optional<IncubationNode> iNode =
                    getBase().getIncubationModule().getIncNode(sender, incNid);
            if (!iNode.isPresent()) {
                throw new IncNodeNotFound("Inc node not found");
            }
            final IncubationNode node = iNode.get();
            final Set<OIdentifiable> commitableRelations = new HashSet<>();
            final LiveNode liveNode = commitNode(sender, node, commitableRelations);

            for (final OIdentifiable commitable : commitableRelations) {
                committableRelationsByNode.put(liveNode.getDocument(), commitable);
            }

            result.put(incNid, liveNode.getNidVer());
            commitInfo.add(new CommitInfo.CommitEntry(node, liveNode));
        }

        /* Commit label */
        for (CommitInfo.CommitEntry entry : commitInfo.getCommitEntrySet()) {
            final Collection<OIdentifiable> additional =
                    committableRelationsByNode.get(entry.getLiveNode().getDocument());
            getBase().getLabelModule().commit(entry.getLiveNode(), additional, commitInfo);
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
