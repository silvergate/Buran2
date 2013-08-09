package com.dcrux.buran.refimpl.baseModules.commit;

import com.dcrux.buran.common.IncNid;
import com.dcrux.buran.common.NidVer;
import com.dcrux.buran.common.UserId;
import com.dcrux.buran.common.Version;
import com.dcrux.buran.common.classDefinition.ClassDefinition;
import com.dcrux.buran.common.classDefinition.ClassFieldsDefinition;
import com.dcrux.buran.common.classes.ClassId;
import com.dcrux.buran.common.exceptions.IncNodeNotFound;
import com.dcrux.buran.common.exceptions.NodeClassNotFoundException;
import com.dcrux.buran.common.fields.FieldIndex;
import com.dcrux.buran.common.fields.typeDef.ITypeDef;
import com.dcrux.buran.common.getterSetter.IDataSetter;
import com.dcrux.buran.refimpl.baseModules.BaseModule;
import com.dcrux.buran.refimpl.baseModules.classes.ClassDefCache;
import com.dcrux.buran.refimpl.baseModules.common.Module;
import com.dcrux.buran.refimpl.baseModules.common.ONid;
import com.dcrux.buran.refimpl.baseModules.common.ONidVer;
import com.dcrux.buran.refimpl.baseModules.deltaRecorder.IRecordPlayer;
import com.dcrux.buran.refimpl.baseModules.fields.FieldConstraintViolationInt;
import com.dcrux.buran.refimpl.baseModules.fields.FieldPerformerRegistry;
import com.dcrux.buran.refimpl.baseModules.fields.ICommitInfo;
import com.dcrux.buran.refimpl.baseModules.fields.IFieldPerformer;
import com.dcrux.buran.refimpl.baseModules.nodeWrapper.FieldIndexAndClassId;
import com.dcrux.buran.refimpl.baseModules.nodeWrapper.IncubationNode;
import com.dcrux.buran.refimpl.baseModules.nodeWrapper.LiveNode;
import com.dcrux.buran.refimpl.baseModules.versions.VersionWrapper;
import com.google.common.base.Optional;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.orientechnologies.orient.core.db.record.OIdentifiable;
import com.orientechnologies.orient.core.id.ORID;
import com.orientechnologies.orient.core.id.ORecordId;
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
            @Nullable final Set<OIdentifiable> outCommittableRelations,
            Collection<CommitResult.IndexResult> outRemoveFromIndexCauseRemoved,
            Collection<CommitResult.IndexResult> outRemoveFromIndexCauseUpdate,
            Collection<CommitResult.IndexResult> outAddToIndex, ONidVer[] outNidVer)
            throws Exception {
        final boolean update = incubationNode.isUpdate();
        if (!update) {
            incubationNode.goLive();
            final LiveNode storeNode = new LiveNode(incubationNode.getDocument());
            storeNode.setVersion(Version.INITIAL);
            incubationNode.getDocument().save();

            /* Add version */
            final VersionWrapper addedVers = getBase().getVersionsModule()
                    .addNodeVersion(incubationNode.getNid(), Version.INITIAL);
            outNidVer[0] = addedVers.getONidVer();
            outAddToIndex.add(new CommitResult.IndexResult(storeNode.getPrimaryClassId(),
                    addedVers.getONidVer()));

            return storeNode;
        } else {
            final ONidVer upOnid = incubationNode.getUpdateNid();
            LiveNode up = getBase().getDataFetchModule().getNode(upOnid);

            playRecordedDeltas(sender, incubationNode, up, outCommittableRelations);

            up.incVersion();
            up.getDocument().save();

            /* Remove old version, add new */
            final VersionWrapper removedVers =
                    getBase().getVersionsModule().removeNodeVersion(upOnid);
            if (up.isMarkedForDeletion()) {
                outRemoveFromIndexCauseRemoved
                        .add(new CommitResult.IndexResult(up.getPrimaryClassId(),
                                removedVers.getONidVer()));
            } else {
                outRemoveFromIndexCauseUpdate
                        .add(new CommitResult.IndexResult(up.getPrimaryClassId(),
                                removedVers.getONidVer()));
            }
            if (!up.isMarkedForDeletion()) {
                /* No need to index deleted nodes */
                final VersionWrapper addedVers =
                        getBase().getVersionsModule().addNodeVersion(up.getNid(), up.getVersion());
                outNidVer[0] = addedVers.getONidVer();
                outAddToIndex.add(new CommitResult.IndexResult(up.getPrimaryClassId(),
                        addedVers.getONidVer()));
            }

            return up;
        }
    }

    public CommitResult commit(final UserId sender, final Collection<IncNid> incNids)
            throws Exception {
        final Map<IncNid, ONidVer> result = new HashMap<>();
        final CommitInfo commitInfo = new CommitInfo();

        final Multimap<OIdentifiable, OIdentifiable> committableRelationsByNode =
                HashMultimap.create();

        Collection<CommitResult.IndexResult> removeFromIndexCauseRemoved = new ArrayList<>();
        Collection<CommitResult.IndexResult> removeFromIndexCauseUpdated = new ArrayList<>();
        Collection<CommitResult.IndexResult> addToIndex = new ArrayList<>();

        final Collection<LiveNode> nodesMarkedAsDeleted = new ArrayList<>();

        /* Commit node */
        for (final IncNid incNid : incNids) {
            final Optional<IncubationNode> iNode =
                    getBase().getIncubationModule().getIncNode(sender, incNid);
            if (!iNode.isPresent()) {
                throw new IncNodeNotFound("Inc node not found");
            }
            final IncubationNode node = iNode.get();
            final Set<OIdentifiable> commitableRelations = new HashSet<>();
            final ONidVer[] onidVers = new ONidVer[1];
            final LiveNode liveNode =
                    commitNode(sender, node, commitableRelations, removeFromIndexCauseRemoved,
                            removeFromIndexCauseUpdated, addToIndex, onidVers);
            if (liveNode.isMarkedForDeletion()) {
                nodesMarkedAsDeleted.add(liveNode);
            }

            for (final OIdentifiable commitable : commitableRelations) {
                committableRelationsByNode.put(liveNode.getDocument(), commitable);
            }

            result.put(incNid, onidVers[0]);
            commitInfo.add(new CommitInfo.CommitEntry(node, liveNode,
                    new NidVer(onidVers[0].getoIdentifiable().toString())));
        }

        /* Validate and commit fields */
        validateAndCommitFields(sender, commitInfo);

        /* Remove incubation */
        for (CommitInfo.CommitEntry entry : commitInfo.getCommitEntrySet()) {
            if (entry.isUpdate()) {
                removeIncNode(new ONid(entry.getIncubationNode().getNid().getRecordId()));
            }
        }

        /* Clear deleted nodes */
        for (final LiveNode nodeMarkedForDeletion : nodesMarkedAsDeleted) {
            /* Remove relations */
            getBase().getNewRelationsModule().removeAllRelations(nodeMarkedForDeletion.getOrid());

            /* Remove node document */
            nodeMarkedForDeletion.deleteNow();
            nodeMarkedForDeletion.getDocument().save();
        }

        return new CommitResult(result, removeFromIndexCauseRemoved, removeFromIndexCauseUpdated,
                addToIndex);
    }

    private void validateAndCommitFields(UserId sender, final CommitInfo commitInfo)
            throws NodeClassNotFoundException, FieldConstraintViolationInt {
             /* Validate and commit nodes */
        final ICommitInfo newCommitInfo = new ICommitInfo() {
            @Override
            public ORID getNidVerByIncNid(ORID incNid) {
                final CommitInfo.CommitEntry cInfoEntry = commitInfo.getByIncNid(incNid);
                if (cInfoEntry == null) {
                    return null;
                }
                return new ORecordId(cInfoEntry.getNidVer().getAsString());
            }

            @Override
            public ORID getNidByIncNid(ORID incNid) {
                final CommitInfo.CommitEntry cInfoEntry = commitInfo.getByIncNid(incNid);
                if (cInfoEntry == null) {
                    return null;
                }
                return cInfoEntry.getLiveNode().getNid().getRecordId();
            }
        };

        final ClassDefCache classDefCache = new ClassDefCache();

        for (CommitInfo.CommitEntry entry : commitInfo.getCommitEntrySet()) {
            final LiveNode node = entry.getLiveNode();
            final ClassId primaryClassId = node.getPrimaryClassId();
            final Set<ClassId> allClassIds = node.getAllClassIds();
            for (final ClassId classId : allClassIds) {
                final ClassDefinition classDef = classDefCache.getClassDef(getBase(), classId);
                for (final Map.Entry<FieldIndex, ClassFieldsDefinition.FieldEntry> fieldEntry :
                        classDef
                        .getFields().getFieldEntries().entrySet()) {
                    final ITypeDef typeDef = fieldEntry.getValue().getTypeDef();
                    final FieldPerformerRegistry registry =
                            getBase().getFieldsModule().getFieldPerformerRegistry();
                    final IFieldPerformer<ITypeDef> performer =
                            (IFieldPerformer<ITypeDef>) registry.get(typeDef.getClass());
                    final FieldIndex fieldIndex = fieldEntry.getKey();
                    final FieldIndexAndClassId fieldIndexAndClassId =
                            new FieldIndexAndClassId(fieldIndex, classId,
                                    primaryClassId.equals(classId));
                    performer.validateAndCommit(getBase(), sender, node, classDef, typeDef,
                            fieldEntry.getValue(), fieldIndexAndClassId, newCommitInfo);
                }
            }
        }
    }

    private void removeIncNode(final ONid incNid) {
        // TODO: Alle felder und so zeugs muss auch weg.
        // TODO: Inc node sollte nicht entfernt werden, sonst kann die ID wieder verwendet werden.
        getBase().getDb().delete(incNid.getRecordId());
    }


}
