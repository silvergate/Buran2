package com.dcrux.buran.refimpl.baseModules.commit;

import com.dcrux.buran.common.*;
import com.dcrux.buran.common.getterSetter.IDataSetter;
import com.dcrux.buran.refimpl.baseModules.BaseModule;
import com.dcrux.buran.refimpl.baseModules.changeTracker.IChangeTracker;
import com.dcrux.buran.refimpl.baseModules.changeTracker.NullChangeTracker;
import com.dcrux.buran.refimpl.baseModules.common.Module;
import com.dcrux.buran.refimpl.baseModules.common.ONid;
import com.dcrux.buran.refimpl.baseModules.deltaRecorder.IRecordPlayer;
import com.dcrux.buran.refimpl.baseModules.nodeWrapper.IncubationNode;
import com.dcrux.buran.refimpl.baseModules.nodeWrapper.LiveNode;
import com.dcrux.buran.refimpl.baseModules.versions.VersionWrapper;
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
            final LiveNode applyTo, @Nullable final Set<OIdentifiable> outCommittableRelations,
            final IChangeTracker changeTracker) throws Exception {
        getBase().getDeltaRecorderModule()
                .playRecords(incubationNode.getNid(), new IRecordPlayer() {

                    @Override
                    public void entry(ONid onid, IDataSetter setter) throws Exception {
                        getBase().getDataMutModule()
                                .setDataDirect(sender, applyTo, setter, outCommittableRelations,
                                        changeTracker);
                    }
                });
    }

    private LiveNode commitNode(final UserId sender, final IncubationNode incubationNode,
            @Nullable final Set<OIdentifiable> outCommittableRelations,
            IChangeTracker changeTracker, Collection<CommitResult.IndexResult> outRemoveFromIndex,
            Collection<CommitResult.IndexResult> outAddToIndex) throws Exception {
        final boolean update = incubationNode.isUpdate();
        if (!update) {
            incubationNode.goLive();
            final LiveNode storeNode = new LiveNode(incubationNode.getDocument());
            storeNode.setVersion(Version.INITIAL);
            incubationNode.getDocument().save();
            changeTracker.newNode(storeNode);

            /* Add version */
            final VersionWrapper addedVers = getBase().getVersionsModule()
                    .addNodeVersion(incubationNode.getNid(), Version.INITIAL);
            outAddToIndex
                    .add(new CommitResult.IndexResult(storeNode.getClassId(), addedVers.getOrid()));

            return storeNode;
        } else {
            final ONid upOnid = incubationNode.getUpdateNid();
            final Version upVersion = incubationNode.getUpdateVersion();
            LiveNode up = getBase().getDataFetchModule().getNodeReq(new NidVer(upOnid, upVersion));

            playRecordedDeltas(sender, incubationNode, up, outCommittableRelations, changeTracker);

            up.incVersion();
            up.getDocument().save();
            changeTracker.updatedNode(up);

            /* Remove old version , add new */
            final VersionWrapper removedVers =
                    getBase().getVersionsModule().removeNodeVersion(upOnid, upVersion);
            outRemoveFromIndex
                    .add(new CommitResult.IndexResult(up.getClassId(), removedVers.getOrid()));
            final VersionWrapper addedVers =
                    getBase().getVersionsModule().addNodeVersion(upOnid, up.getVersion());
            outAddToIndex.add(new CommitResult.IndexResult(up.getClassId(), addedVers.getOrid()));

            return up;
        }
    }

    public CommitResult commit(final UserId sender, final Collection<IIncNid> incNids)
            throws Exception {
        final Map<IIncNid, NidVer> result = new HashMap<>();
        final CommitInfo commitInfo = new CommitInfo();

        final Multimap<OIdentifiable, OIdentifiable> committableRelationsByNode =
                HashMultimap.create();

        Collection<CommitResult.IndexResult> removeFromIndex = new ArrayList<>();
        Collection<CommitResult.IndexResult> addToIndex = new ArrayList<>();

        @Deprecated
        final IChangeTracker changeTracker = NullChangeTracker.SINGLETON;

        /* Commit node */
        for (final IIncNid incNid : incNids) {
            final Optional<IncubationNode> iNode =
                    getBase().getIncubationModule().getIncNode(sender, incNid);
            if (!iNode.isPresent()) {
                throw new IncNodeNotFound("Inc node not found");
            }
            final IncubationNode node = iNode.get();
            final Set<OIdentifiable> commitableRelations = new HashSet<>();
            final LiveNode liveNode =
                    commitNode(sender, node, commitableRelations, changeTracker, removeFromIndex,
                            addToIndex);

            for (final OIdentifiable commitable : commitableRelations) {
                committableRelationsByNode.put(liveNode.getDocument(), commitable);
            }

            result.put(incNid, liveNode.getNidVer());
            commitInfo.add(new CommitInfo.CommitEntry(node, liveNode));
        }

        /* Commit edge */
        for (CommitInfo.CommitEntry entry : commitInfo.getCommitEntrySet()) {
            final Collection<OIdentifiable> additional =
                    committableRelationsByNode.get(entry.getLiveNode().getDocument());
            getBase().getEdgeModule()
                    .commit(entry.getLiveNode(), additional, commitInfo, changeTracker);
        }

        /* Remove incubation */
        for (CommitInfo.CommitEntry entry : commitInfo.getCommitEntrySet()) {
            if (entry.isUpdate()) {
                removeIncNode(new ONid(entry.getIncubationNode().getNid().getRecordId()));
            }
        }

        return new CommitResult(result, removeFromIndex, addToIndex);
    }

    private void removeIncNode(final ONid incNid) {
        // TODO: Alle felder und so zeugs muss auch weg.
        getBase().getDb().delete(incNid.getRecordId());
    }


}
