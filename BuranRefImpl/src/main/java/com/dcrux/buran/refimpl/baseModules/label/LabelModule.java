package com.dcrux.buran.refimpl.baseModules.label;

import com.dcrux.buran.common.labels.*;
import com.dcrux.buran.common.labels.getter.GetLabel;
import com.dcrux.buran.common.labels.getter.GetLabelResult;
import com.dcrux.buran.common.labels.setter.SetLabel;
import com.dcrux.buran.refimpl.baseModules.BaseModule;
import com.dcrux.buran.refimpl.baseModules.commit.CommitInfo;
import com.dcrux.buran.refimpl.baseModules.common.Module;
import com.dcrux.buran.refimpl.baseModules.common.ONid;
import com.dcrux.buran.refimpl.baseModules.nodeWrapper.CommonNode;
import com.dcrux.buran.refimpl.baseModules.nodeWrapper.LiveNode;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.orientechnologies.orient.core.db.record.OIdentifiable;
import com.orientechnologies.orient.core.index.OIndex;
import com.orientechnologies.orient.core.index.OIndexManagerProxy;
import com.orientechnologies.orient.core.iterator.ORecordIteratorClass;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.sun.istack.internal.Nullable;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Buran.
 *
 * @author: ${USER} Date: 29.06.13 Time: 13:59
 */
public class LabelModule extends Module<BaseModule> {

    //public static final String INDEX_INC_NODES = "incNodexIdx";
    //public static final String INDEX_NODES_BY_LABEL = "nodesByLabel";

    public LabelModule(BaseModule baseModule) {
        super(baseModule);
    }

    public void setupDb() {
        RelationWrapper.setupDb(getBase());
    }

    public <T extends Serializable> T performLabelGet(final LiveNode node, ILabelGet<T> labelGet) {
        if (labelGet instanceof GetLabel) {
            final GetLabel getLabel = (GetLabel) labelGet;
            return (T) performLabelGet_getLabel(node, getLabel);
        } else {
            throw new NotImplementedException();
        }
    }

    private GetLabelResult performLabelGet_getLabel(final LiveNode node, final GetLabel getLabel) {
        if (!(getLabel.getLabelName() instanceof ClassLabelName)) {
            throw new NotImplementedException();
        }
        final ClassLabelName classLabelName = (ClassLabelName) getLabel.getLabelName();

        final OIndex<?> index = getBase().getDbUtils()
                .getIndex(RelationWrapper.CLASS_NAME, RelationWrapper.INDEX_NODES_BY_LABEL);
        final Object valueStart = index.getDefinition()
                .createValue(true, node.getNid().getRecordId().toString(),
                        classLabelName.getIndex(), getLabel.getFromIndex().getIndex());
        final Object valueEnd = index.getDefinition()
                .createValue(true, node.getNid().getRecordId().toString(),
                        classLabelName.getIndex(), getLabel.getToIndex().getIndex());
        final Collection<OIdentifiable> foundSet = index.getValuesBetween(valueStart, valueEnd);
        System.out.println("Found labels: " + foundSet);
        Multimap<LabelIndex, ILabelTarget> results = HashMultimap.create();
        for (OIdentifiable found : foundSet) {
            final ODocument doc = getBase().getDb().load(found.getIdentity());
            final RelationWrapper relationWrapper = new RelationWrapper(doc);
            final ILabelTarget labelTarget = RelWrapToLabelTarget.convert(relationWrapper);
            results.put(relationWrapper.getLabelIndex(), labelTarget);
        }
        return new GetLabelResult(results);
    }

    public void performLabelSet(final CommonNode incubationNode, ILabelSet labelSet,
            @Nullable Set<OIdentifiable> outCommitableRelations) {
        if (labelSet instanceof SetLabel) {
            final SetLabel setLabel = (SetLabel) labelSet;
            final ILabelName labelName = setLabel.getName();
            if (labelName instanceof ClassLabelName) {
                final ClassLabelName classLabelName = (ClassLabelName) labelName;
                for (final Map.Entry<LabelIndex, ILabelTargetInc> target : setLabel.getTargets()
                        .entrySet()) {
                    final ILabelTargetInc labelTargetInc = target.getValue();
                    final LabelIndex index = target.getKey();
                    final RelationWrapper relationWrapper = RelationWrapper
                            .from(incubationNode.getNid(), classLabelName, index, labelTargetInc);

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
        throw new IllegalArgumentException("Unknown set label");
    }

    public void commit(CommonNode node, Collection<OIdentifiable> additionalRelations,
            CommitInfo commitInfo) {
        /* Make labels live */
        //ONid incNid = incNode.getNid();
        //ONid liveNid = liveNode.getNid();
        makeLabelsLive(node.getNid(), commitInfo, additionalRelations);
    }

    /*private void removeAllLabels(ONid nid) {
        final OIndex<?> index =
                getBase().getDbUtils().getIndex(RelationWrapper.CLASS_NAME, INDEX_NODES_BY_LABEL);
        final Object value = index.getDefinition().createValue(nid.getRecordId().toString());
        final Collection<OIdentifiable> foundSet = index.getValuesBetween(value, value);
        System.out.println("Found (remove old labels): " + foundSet);
        for (final OIdentifiable found : foundSet) {
            final ODocument doc = getBase().getDb().load(found.getIdentity());
            doc.delete();
        }
    } */

    private void makeLabelsLive(ONid nid, CommitInfo commitInfo,
            @Nullable Collection<OIdentifiable> additionalRelations) {
        final OIndexManagerProxy indexManager = getBase().getDb().getMetadata().getIndexManager();
        final OIndex<?> index = indexManager
                .getClassIndex(RelationWrapper.CLASS_NAME, RelationWrapper.INDEX_INC_NODES);
        final Object value = index.getDefinition().createValue(true, nid.getAsString());

        /* Test */
        /*final Iterator<OIdentifiable> titer = index.valuesIterator();
        System.out.println("NumOfEntries in Index: " + index.getSize());
        while (titer.hasNext()) {
            System.out.println("Entry : " + titer.next());
        } */
        /* Test */
        final ORecordIteratorClass<ODocument> iter =
                getBase().getDb().browseClass(RelationWrapper.CLASS_NAME);
        while (iter.hasNext()) {
            System.out.println("   * Relation in DB: " + iter.next());
        }

        final Collection<OIdentifiable> foundSet = index.getValuesBetween(value, value);
        System.out.println("Found (make live): For Node " + nid.getAsString() + ", " +
                "" + foundSet + ", index-size: " + index.getSize());
        for (final OIdentifiable found : foundSet) {
            makeSingleLabelLive(found, commitInfo);
        }
        if (additionalRelations != null) {
            for (final OIdentifiable found : additionalRelations) {
                makeSingleLabelLive(found, commitInfo);
            }
        }
    }

    private void makeSingleLabelLive(OIdentifiable found, CommitInfo commitInfo) {
        final ODocument doc = getBase().getDb().load(found.getIdentity());
        final RelationWrapper relationWrapper = new RelationWrapper(doc);

            /* Adjust target */
        final LiveNode newTarget;
        if (relationWrapper.isTargetIsInc()) {
            CommitInfo.CommitEntry possibleNewEntry =
                    commitInfo.getByIncNid(relationWrapper.getTarget().getRecordId());
            newTarget = possibleNewEntry.getLiveNode();
        } else {
            newTarget = null;
        }

        relationWrapper.goLive(newTarget);
        relationWrapper.getDocument().save();
    }
}
