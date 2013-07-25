package com.dcrux.buran.refimpl.baseModules.edge;

import com.dcrux.buran.common.INidOrNidVer;
import com.dcrux.buran.common.NidVer;
import com.dcrux.buran.common.UserId;
import com.dcrux.buran.common.classes.ClassId;
import com.dcrux.buran.common.edges.*;
import com.dcrux.buran.common.edges.getter.GetEdge;
import com.dcrux.buran.common.edges.getter.GetEdgeResult;
import com.dcrux.buran.common.edges.getter.GetInClassEdge;
import com.dcrux.buran.common.edges.getter.GetInClassEdgeResult;
import com.dcrux.buran.common.edges.setter.SetEdge;
import com.dcrux.buran.common.exceptions.IncNodeNotFound;
import com.dcrux.buran.common.exceptions.NodeNotFoundException;
import com.dcrux.buran.refimpl.baseModules.BaseModule;
import com.dcrux.buran.refimpl.baseModules.changeTracker.IChangeTracker;
import com.dcrux.buran.refimpl.baseModules.commit.CommitInfo;
import com.dcrux.buran.refimpl.baseModules.common.IfaceUtils;
import com.dcrux.buran.refimpl.baseModules.common.Module;
import com.dcrux.buran.refimpl.baseModules.common.ONid;
import com.dcrux.buran.refimpl.baseModules.nodeWrapper.CommonNode;
import com.dcrux.buran.refimpl.baseModules.nodeWrapper.LiveNode;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.orientechnologies.orient.core.db.record.OIdentifiable;
import com.orientechnologies.orient.core.index.OIndex;
import com.orientechnologies.orient.core.index.OIndexManagerProxy;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.sun.istack.internal.Nullable;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.*;

/**
 * Buran.
 *
 * @author: ${USER} Date: 29.06.13 Time: 13:59
 */
public class EdgeModule extends Module<BaseModule> {

    public EdgeModule(BaseModule baseModule) {
        super(baseModule);
    }

    public void setupDb() {
        RelationWrapper.setupDb(getBase());
    }

    public <T extends Serializable> T performLabelGet(final LiveNode node, IEdgeGetter<T> labelGet)
            throws NodeNotFoundException {
        if (labelGet instanceof GetEdge) {
            final GetEdge getEdge = (GetEdge) labelGet;
            return (T) performLabelGet_getLabel(node, getEdge);
        }

        if (labelGet instanceof GetInClassEdge) {
            final GetInClassEdge getInClassEdge = (GetInClassEdge) labelGet;
            return (T) performLabelGet_getInClassEdge(node, getInClassEdge);
        }

        throw new NotImplementedException();
    }

    private GetEdgeResult performLabelGet_getLabel(final LiveNode node, final GetEdge getEdge) {
        if (!(getEdge.getLabelName() instanceof ClassLabelName)) {
            throw new NotImplementedException();
        }
        final ClassLabelName classLabelName = (ClassLabelName) getEdge.getLabelName();

        final OIndex<?> index = getBase().getDbUtils()
                .getIndex(RelationWrapper.CLASS_NAME, RelationWrapper.INDEX_NODES_BY_LABEL);
        final Object valueStart = index.getDefinition()
                .createValue(true, node.getNid().getRecordId().toString(),
                        classLabelName.getIndex(), getEdge.getFromIndex().getIndex());
        final Object valueEnd = index.getDefinition()
                .createValue(true, node.getNid().getRecordId().toString(),
                        classLabelName.getIndex(), getEdge.getToIndex().getIndex());
        final Collection<OIdentifiable> foundSet = index.getValuesBetween(valueStart, valueEnd);

        System.out.println("Found edges: " + foundSet);
        Multimap<LabelIndex, IEdgeTarget> results = HashMultimap.create();
        for (OIdentifiable found : foundSet) {
            final ODocument doc = getBase().getDb().load(found.getIdentity());
            final RelationWrapper relationWrapper = new RelationWrapper(doc);
            final IEdgeTarget labelTarget = RelWrapToLabelTarget.convert(relationWrapper);
            results.put(relationWrapper.getLabelIndex(), labelTarget);
        }
        return new GetEdgeResult(results);
    }

    private GetInClassEdgeResult performLabelGet_getInClassEdge(LiveNode node,
            GetInClassEdge getInClassEdge) throws NodeNotFoundException {

        final Object valueStart;
        final Object valueEnd;
        final OIndex<?> index;

        if (getInClassEdge.getSourceNode().isPresent()) {
            /* Case one: Source node is given */
            final INidOrNidVer optSrcNode = getInClassEdge.getSourceNode().get();
            final LiveNode srcNode = getBase().getDataFetchModule().getNode(optSrcNode);
            final ClassId classId = srcNode.getClassId();
            final short classLabelName;
            final int targetType;
            if (getInClassEdge.isVersioned()) {
                targetType = RelationWrapper.TargetType2.versioned.getValue();
            } else {
                targetType = RelationWrapper.TargetType2.unversioned.getValue();
            }
            if (getInClassEdge.getLabelName() instanceof ClassLabelName) {
                classLabelName = ((ClassLabelName) getInClassEdge.getLabelName()).getIndex();
            } else {
                throw new IllegalArgumentException("Pub Label names not yet implemented");
            }

            index = getBase().getDbUtils().getIndex(RelationWrapper.CLASS_NAME,
                    RelationWrapper.INDEX_IN_EDGES_WITH_SRC_NODE);
            valueStart = index.getDefinition()
                    .createValue(true, node.getNid().getAsString(), classLabelName, targetType,
                            srcNode.getNid().getAsString(),
                            getInClassEdge.getFromIndex().getIndex());
            valueEnd = index.getDefinition()
                    .createValue(true, node.getNid().getAsString(), classLabelName, targetType,
                            srcNode.getNid().getAsString(), getInClassEdge.getToIndex().getIndex());
        } else if (getInClassEdge.getSourceClassId().isPresent()) {
            /* Case two: Source class is given */
            final ClassId classId = getInClassEdge.getSourceClassId().get();
            final short classLabelName;
            final int targetType;
            if (getInClassEdge.isVersioned()) {
                targetType = RelationWrapper.TargetType2.versioned.getValue();
            } else {
                targetType = RelationWrapper.TargetType2.unversioned.getValue();
            }
            if (getInClassEdge.getLabelName() instanceof ClassLabelName) {
                classLabelName = ((ClassLabelName) getInClassEdge.getLabelName()).getIndex();
            } else {
                throw new IllegalArgumentException("Pub Label names not yet implemented");
            }

            index = getBase().getDbUtils().getIndex(RelationWrapper.CLASS_NAME,
                    RelationWrapper.INDEX_IN_EDGES_WITH_SRC_CLASS);
            valueStart = index.getDefinition()
                    .createValue(true, node.getNid().getAsString(), classLabelName, targetType,
                            classId.getId(), getInClassEdge.getFromIndex().getIndex());
            valueEnd = index.getDefinition()
                    .createValue(true, node.getNid().getAsString(), classLabelName, targetType,
                            classId.getId(), getInClassEdge.getToIndex().getIndex());
        } else {
            /* Case three: Public label, no classId, no node */
            final String classLabelName;
            final int targetType;
            if (getInClassEdge.isVersioned()) {
                targetType = RelationWrapper.TargetType2.versioned.getValue();
            } else {
                targetType = RelationWrapper.TargetType2.unversioned.getValue();
            }
            if (getInClassEdge.getLabelName() instanceof PublicLabelName) {
                classLabelName = ((PublicLabelName) getInClassEdge.getLabelName()).getName();
            } else {
                throw new IllegalArgumentException("No class, no node, need a public label");
            }

            index = getBase().getDbUtils().getIndex(RelationWrapper.CLASS_NAME,
                    RelationWrapper.INDEX_IN_EDGES_WITH_PUB_LABEL);
            valueStart = index.getDefinition()
                    .createValue(true, node.getNid().getAsString(), classLabelName, targetType,
                            getInClassEdge.getFromIndex().getIndex());
            valueEnd = index.getDefinition()
                    .createValue(true, node.getNid().getAsString(), classLabelName, targetType,
                            getInClassEdge.getToIndex().getIndex());
        }

        final Collection<OIdentifiable> foundSet = index.getValuesBetween(valueStart, valueEnd);

        System.out.println("Found in edges: " + foundSet + ", in index: " + index.getSize());
        final List<GetInClassEdgeResult.Entry> entries = new ArrayList<>();
        for (OIdentifiable found : foundSet) {
            final ODocument doc = getBase().getDb().load(found.getIdentity());
            final RelationWrapper relationWrapper = new RelationWrapper(doc);
            entries.add(
                    new GetInClassEdgeResult.Entry(IfaceUtils.toOutput(relationWrapper.getSource()),
                            relationWrapper.getLabelIndex()));
        }
        return new GetInClassEdgeResult(entries);
    }

    public void performLabelSet(final UserId sender, final CommonNode incubationNode,
            IEdgeSetter labelSet, @Nullable Set<OIdentifiable> outCommitableRelations)
            throws NodeNotFoundException, IncNodeNotFound {
        if (labelSet instanceof SetEdge) {
            final SetEdge setEdge = (SetEdge) labelSet;
            final ILabelName labelName = setEdge.getName();
            if (labelName instanceof ClassLabelName) {
                final ClassLabelName classLabelName = (ClassLabelName) labelName;
                for (final Map.Entry<LabelIndex, IEdgeTargetInc> target : setEdge.getTargets()
                        .entrySet()) {
                    final IEdgeTargetInc labelTargetInc = target.getValue();
                    final LabelIndex index = target.getKey();
                    final RelationWrapper relationWrapper = RelationWrapper
                            .from(sender, incubationNode.getNid(), incubationNode.getClassId(),
                                    classLabelName, index, labelTargetInc, getBase());

                    //TODO: Überprüfen, ob targets verfügbar ist (zwar: Eher erst beim
                    // commitOld).
                    //TODO: Ausserdem schauen, dass nicht auf eigner account verweist wird (bei
                    // Externem target).
                    relationWrapper.getDocument().save();
                    if (outCommitableRelations != null) {
                        outCommitableRelations.add(relationWrapper.getDocument());
                    }

                    System.out.println("Successfully added a new Relation from Node: " +
                            incubationNode.getNid().getAsString());
                }
                return;
            }
            throw new IllegalArgumentException("Only Class Label names are " + "implemented");
        }
        throw new IllegalArgumentException("Unknown set edge");
    }

    public void commit(CommonNode node, Collection<OIdentifiable> additionalRelations,
            CommitInfo commitInfo, IChangeTracker changeTracker) {
        /* Make edges live */
        makeLabelsLive(node, commitInfo, additionalRelations, changeTracker);
    }

    /*private void removeAllLabels(ONid nid) {
        final OIndex<?> indexAndNotify =
                getBase().getDbUtils().getIndex(RelationWrapper.CLASS_NAME, INDEX_NODES_BY_LABEL);
        final Object value = indexAndNotify.getDefinition().createValue(nid.getRecordId()
        .toString());
        final Collection<OIdentifiable> foundSet = indexAndNotify.getValuesBetween(value, value);
        System.out.println("Found (remove old edges): " + foundSet);
        for (final OIdentifiable found : foundSet) {
            final ODocument doc = getBase().getDb().load(found.getIdentity());
            doc.delete();
        }
    } */

    private void makeLabelsLive(CommonNode node, CommitInfo commitInfo,
            @Nullable Collection<OIdentifiable> additionalRelations, IChangeTracker changeTracker) {
        final ONid nid = node.getNid();
        final OIndexManagerProxy indexManager = getBase().getDb().getMetadata().getIndexManager();
        final OIndex<?> index = indexManager
                .getClassIndex(RelationWrapper.CLASS_NAME, RelationWrapper.INDEX_INC_NODES);
        final Object value = index.getDefinition().createValue(true, nid.getAsString());

        /* Test */
        /*final Iterator<OIdentifiable> titer = indexAndNotify.valuesIterator();
        System.out.println("NumOfEntries in Index: " + indexAndNotify.getSize());
        while (titer.hasNext()) {
            System.out.println("Entry : " + titer.next());
        } */

        /* Test */
        /*final ORecordIteratorClass<ODocument> iter =
                getBase().getDb().browseClass(RelationWrapper.CLASS_NAME);
        while (iter.hasNext()) {
            System.out.println("   * Relation in DB: " + iter.next());
        }*/

        final LiveNode liveNode = new LiveNode(node.getDocument());

        final Collection<OIdentifiable> foundSet = index.getValuesBetween(value, value);
        /*System.out.println("Found (make live): For Node " + nid.getAsString() + ", " +
                "" + foundSet + ", indexAndNotify-size: " + indexAndNotify.getSize());*/
        for (final OIdentifiable found : foundSet) {
            makeSingleLabelLive(liveNode, found, commitInfo, changeTracker);
        }
        if (additionalRelations != null) {
            for (final OIdentifiable found : additionalRelations) {
                makeSingleLabelLive(liveNode, found, commitInfo, changeTracker);
            }
        }
    }

    private void makeSingleLabelLive(LiveNode source, OIdentifiable found, CommitInfo commitInfo,
            IChangeTracker changeTracker) {
        final ODocument doc = getBase().getDb().load(found.getIdentity());
        final RelationWrapper relationWrapper = new RelationWrapper(doc);

            /* Adjust target */
        final LiveNode newTarget;
        final NidVer newTargetNidVer;
        if (relationWrapper.isTargetIsInc()) {
            CommitInfo.CommitEntry possibleNewEntry =
                    commitInfo.getByIncNid(relationWrapper.getTarget().getRecordId());
            newTarget = possibleNewEntry.getLiveNode();
            newTargetNidVer = possibleNewEntry.getNidVer();
            if (newTarget == null) {
                throw new IllegalArgumentException(MessageFormat
                        .format("Node with IncNid {0} not " +
                                "found in commit. Node need to be in commit!",
                                relationWrapper.getTarget().getRecordId()));
            }
        } else {
            newTarget = null;
            newTargetNidVer = null;
        }

        relationWrapper.goLive(newTarget, newTargetNidVer);
        relationWrapper.getDocument().save();

        changeTracker.newRelation(source, relationWrapper);
    }
}
