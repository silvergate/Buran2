package com.dcrux.buran.refimpl.baseModules.label;

import com.dcrux.buran.common.labels.*;
import com.dcrux.buran.common.labels.getter.GetLabel;
import com.dcrux.buran.common.labels.getter.GetLabelResult;
import com.dcrux.buran.common.labels.setter.SetLabel;
import com.dcrux.buran.refimpl.baseModules.BaseModule;
import com.dcrux.buran.refimpl.baseModules.commit.CommitInfo;
import com.dcrux.buran.refimpl.baseModules.common.Module;
import com.dcrux.buran.refimpl.baseModules.common.ONid;
import com.dcrux.buran.refimpl.baseModules.nodeWrapper.IncubationNode;
import com.dcrux.buran.refimpl.baseModules.nodeWrapper.LiveNode;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.orientechnologies.orient.core.db.record.OIdentifiable;
import com.orientechnologies.orient.core.index.OIndex;
import com.orientechnologies.orient.core.index.OIndexManagerProxy;
import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.metadata.schema.OSchema;
import com.orientechnologies.orient.core.metadata.schema.OType;
import com.orientechnologies.orient.core.record.impl.ODocument;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * Buran.
 *
 * @author: ${USER} Date: 29.06.13 Time: 13:59
 */
public class LabelModule extends Module<BaseModule> {

    public static final String INDEX_INC_NODES = "incNodexIdx";
    public static final String INDEX_NODES_BY_LABEL = "nodesByLabel";

    public LabelModule(BaseModule baseModule) {
        super(baseModule);
    }

    public void setupDb() {
        final OSchema schema = getBase().getDb().getMetadata().getSchema();
        if (!schema.existsClass(RelationWrapper.CLASS_NAME)) {
            final OClass clazz = schema.createClass(RelationWrapper.CLASS_NAME);
            clazz.createProperty(RelationWrapper.FIELD_SRC_IS_INC, OType.BOOLEAN);
            clazz.createProperty(RelationWrapper.FIELD_LABEL_NAME, OType.SHORT);
            clazz.createProperty(RelationWrapper.FIELD_LABEL_INDEX, OType.LONG);
            clazz.createProperty(RelationWrapper.FIELD_SRC, OType.STRING);

            clazz.createIndex(INDEX_INC_NODES, OClass.INDEX_TYPE.NOTUNIQUE,
                    RelationWrapper.FIELD_SRC_IS_INC, RelationWrapper.FIELD_SRC);
            final OIndex index =
                    clazz.createIndex(INDEX_NODES_BY_LABEL, OClass.INDEX_TYPE.NOTUNIQUE,
                            RelationWrapper.FIELD_SRC, RelationWrapper.FIELD_LABEL_NAME,
                            RelationWrapper.FIELD_LABEL_INDEX);
            schema.save();
        }
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

        final OIndex<?> index =
                getBase().getDbUtils().getIndex(RelationWrapper.CLASS_NAME, INDEX_NODES_BY_LABEL);
        final Object valueStart = index.getDefinition()
                .createValue(node.getNid().getRecordId().toString(), classLabelName.getIndex(),
                        getLabel.getFromIndex().getIndex());
        final Object valueEnd = index.getDefinition()
                .createValue(node.getNid().getRecordId().toString(), classLabelName.getIndex(),
                        getLabel.getToIndex().getIndex());
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

    public void performLabelSet(final IncubationNode incubationNode, ILabelSet labelSet) {
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

                    System.out.println("Successfully added a new Relation");
                }
                return;
            }
            throw new IllegalArgumentException("Only Class Label names are " + "implemented");
        }
        throw new IllegalArgumentException("Unknown set label");
    }

    public void commit(IncubationNode incNode, LiveNode liveNode, CommitInfo commitInfo) {
        /* Remove old labels */
        if (!incNode.getNid().getRecordId().equals(liveNode.getNid().getRecordId())) {
            /* Is update */
            removeAllLabels(incNode.getUpdateNid());
        }
        /* Make labels live */
        ONid incNid = incNode.getNid();
        ONid liveNid = liveNode.getNid();
        makeLabelsLive(incNid, liveNid, commitInfo);

    }

    private void removeAllLabels(ONid nid) {
        final OIndexManagerProxy indexManager = getBase().getDb().getMetadata().getIndexManager();
        final OIndex<?> index =
                getBase().getDbUtils().getIndex(RelationWrapper.CLASS_NAME, INDEX_NODES_BY_LABEL);
        final Object value = index.getDefinition().createValue(nid.getRecordId().toString());
        final Collection<OIdentifiable> foundSet = index.getValuesBetween(value, value);
        System.out.println("Found (remove old labels): " + foundSet);
        for (final OIdentifiable found : foundSet) {
            final ODocument doc = getBase().getDb().load(found.getIdentity());
            doc.delete();
        }
    }

    private void makeLabelsLive(ONid incNid, ONid liveNid, CommitInfo commitInfo) {
        final OIndexManagerProxy indexManager = getBase().getDb().getMetadata().getIndexManager();
        final OIndex<?> index =
                indexManager.getClassIndex(RelationWrapper.CLASS_NAME, INDEX_INC_NODES);
        final Object value =
                index.getDefinition().createValue(true, incNid.getRecordId().toString());

        /* Test */
        final Iterator<OIdentifiable> titer = index.valuesIterator();
        System.out.println("NumOfEntries in Index: " + index.getSize());
        while (titer.hasNext()) {
            System.out.println("Entry : " + titer.next());
        }

        final Collection<OIdentifiable> foundSet = index.getValuesBetween(value, value);
        System.out.println("Found (make live): " + foundSet);
        for (final OIdentifiable found : foundSet) {
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

            relationWrapper.goLive(liveNid, newTarget);
            relationWrapper.getDocument().save();
        }
    }
}
