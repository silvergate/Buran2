package com.dcrux.buran.refimpl.baseModules.edge;

import com.dcrux.buran.common.INid;
import com.dcrux.buran.common.UserId;
import com.dcrux.buran.common.Version;
import com.dcrux.buran.common.classes.ClassId;
import com.dcrux.buran.common.edges.ClassLabelName;
import com.dcrux.buran.common.edges.IEdgeTargetInc;
import com.dcrux.buran.common.edges.LabelIndex;
import com.dcrux.buran.common.edges.targets.EdgeTarget;
import com.dcrux.buran.common.edges.targets.EdgeTargetExt;
import com.dcrux.buran.common.edges.targets.EdgeTargetInc;
import com.dcrux.buran.refimpl.baseModules.BaseModule;
import com.dcrux.buran.refimpl.baseModules.common.IfaceUtils;
import com.dcrux.buran.refimpl.baseModules.common.ONid;
import com.dcrux.buran.refimpl.baseModules.nodeWrapper.LiveNode;
import com.orientechnologies.orient.core.id.ORecordId;
import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.metadata.schema.OSchema;
import com.orientechnologies.orient.core.metadata.schema.OType;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.sun.istack.internal.Nullable;

/**
 * Buran.
 *
 * @author: ${USER} Date: 01.07.13 Time: 02:21
 */
public class RelationWrapper {

    public static enum TargetType {
        liveTarget,
        incubationUnversioned,
        incubationVersioned
    }

    private final ODocument document;

    public RelationWrapper(ODocument document) {
        this.document = document;
    }

    public static final String INDEX_INC_NODES = "incNodexIdx";
    public static final String INDEX_NODES_BY_LABEL = "nodesByLabel";
    public static final String INDEX_IN_EDGES = "inEdgesIndex";

    public static final String CLASS_NAME = "ClassRelation";

    /* Source */
    public static final String FIELD_SRC = "src";
    public static final String FIELD_SRC_CLASS = "srcCls";

    /* Label and index */
    public static final String FIELD_LABEL_NAME = "labelName";
    public static final String FIELD_LABEL_INDEX = "labelIndex";

    /* Target adjustment */
    public static final String FIELD_UNCOMMITTED = "isuncom";
    public static final String FIELD_ISCOM = "iscom";


    /* Target */
    public static final String FIELD_TARGET_IS_INC = "tinc";
    public static final String FIELD_TARGET = "target";
    public static final String FIELD_TARGET_VERSION = "tver";
    public static final String FIELD_UPDATE_VESION_ON_COMMIT = "uvoc";
    public static final String FIELD_TARGET_USERID = "tuid";

    public static RelationWrapper from(ONid source, ClassId sourceClass, ClassLabelName labelName,
            LabelIndex index, IEdgeTargetInc targetInc) {
        final ONid target;
        final @Nullable Version targetVersion;
        final @Nullable UserId targetUserId;
        final TargetType targetType;

        if (targetInc instanceof EdgeTarget) {
            final EdgeTarget labelTarget = (EdgeTarget) targetInc;
            target = IfaceUtils.getONid(labelTarget.getTargetNid());
            targetVersion = labelTarget.getVersion().orNull();
            targetUserId = null;
            targetType = TargetType.liveTarget;
        } else if (targetInc instanceof EdgeTargetExt) {
            EdgeTargetExt labelTargetExt = (EdgeTargetExt) targetInc;
            target = IfaceUtils.getONid(labelTargetExt.getTargetNid());
            targetVersion = labelTargetExt.getVersion().orNull();
            targetUserId = labelTargetExt.getUserId();
            targetType = TargetType.liveTarget;
        } else if (targetInc instanceof EdgeTargetInc) {
            EdgeTargetInc labelTargeInc = (EdgeTargetInc) targetInc;

            target = IfaceUtils.getOincNid(labelTargeInc.getTargetNid());
            targetVersion = null;
            targetUserId = null;
            if (labelTargeInc.isVersioned()) targetType = TargetType.incubationVersioned;
            else targetType = TargetType.incubationUnversioned;
        } else {
            throw new IllegalArgumentException("Unknown target type");
        }

        return createInc(source, sourceClass, target, targetVersion, targetUserId, targetType,
                labelName, index);
    }

    private static RelationWrapper createInc(ONid source, ClassId srcClass, ONid target,
            @Nullable Version targetVersion, @Nullable UserId targetUserId, TargetType targetType,
            ClassLabelName labelName, LabelIndex index) {
        ODocument doc = new ODocument(CLASS_NAME);

        doc.field(FIELD_SRC_CLASS, srcClass.getId(), OType.LONG);
        doc.field(FIELD_LABEL_NAME, labelName.getIndex(), OType.SHORT);
        doc.field(FIELD_LABEL_INDEX, index.getIndex(), OType.LONG);
        RelationWrapper relationWrapper = new RelationWrapper(doc);
        relationWrapper.setSource(source);

        /* Commit state */
        doc.field(FIELD_UNCOMMITTED, true, OType.BOOLEAN);

        /* Target */
        doc.field(FIELD_TARGET, target.getAsString(), OType.STRING);
        if (targetVersion != null) {
            doc.field(FIELD_TARGET_VERSION, targetVersion.getVersion(), OType.LONG);

        }
        switch (targetType) {
            case liveTarget:
                break;
            case incubationUnversioned:
                doc.field(FIELD_TARGET_IS_INC, true, OType.BOOLEAN);
                break;
            case incubationVersioned:
                doc.field(FIELD_TARGET_IS_INC, true, OType.BOOLEAN);
                doc.field(FIELD_UPDATE_VESION_ON_COMMIT, true, OType.BOOLEAN);
                break;
        }
        if (targetUserId != null) {
            doc.field(FIELD_TARGET_USERID, targetUserId.getId(), OType.LONG);
        }

        return relationWrapper;
    }

    public ODocument getDocument() {
        return document;
    }

    protected void setSource(ONid src) {
        getDocument().field(FIELD_SRC, src.getRecordId().toString(), OType.STRING);
    }

    @Nullable
    public INid getSource() {
        final String oIdentifiable = getDocument().field(FIELD_SRC, OType.STRING);
        if (oIdentifiable == null) {
            return null;
        }
        return ONid.fromString(oIdentifiable);
    }

    public boolean isTargetIsInc() {
        final Boolean targetInc = getDocument().field(FIELD_TARGET_IS_INC, OType.BOOLEAN);
        return (targetInc != null) && (targetInc);
    }

    @Nullable
    public ClassLabelName getClassLabelName() {
        final Short classLabel = getDocument().field(FIELD_LABEL_NAME, OType.SHORT);
        if (classLabel == null) {
            return null;
        }
        return new ClassLabelName(classLabel);
    }

    public LabelIndex getLabelIndex() {
        final Long labelIndex = getDocument().field(FIELD_LABEL_INDEX, OType.LONG);
        if (labelIndex == null) {
            return null;
        }
        return new LabelIndex(labelIndex);
    }

    protected void setTarget(ONid target) {
        getDocument().field(FIELD_TARGET, target.getAsString(), OType.STRING);
    }

    public ONid getTarget() {
        final String target = getDocument().field(FIELD_TARGET, OType.STRING);
        if (target == null) {
            return null;
        }

        return new ONid(new ORecordId(target));
    }

    @Nullable
    public UserId getTargetUserId() {
        final Long uid = getDocument().field(FIELD_TARGET_USERID, OType.LONG);
        if (uid == null) {
            return null;
        }
        return new UserId(uid);
    }

    public Version getTargetVersion() {
        final Long version = getDocument().field(FIELD_TARGET_VERSION, OType.LONG);
        if (version == null) {
            return null;
        }
        return new Version(version);
    }

    public void setTargetVersion(Version version) {
        getDocument().field(FIELD_TARGET_VERSION, version.getVersion(), OType.LONG);
    }

    public boolean isUpdateVersionOnCommit() {
        Boolean updateOnCommit = getDocument().field(FIELD_UPDATE_VESION_ON_COMMIT, OType.BOOLEAN);
        return (updateOnCommit != null) && (updateOnCommit);
    }

    public void goLive(@Nullable LiveNode committedTarget) {
        /* Adjust target */
        if (isTargetIsInc()) {
            if (committedTarget == null) {
                throw new IllegalStateException("Label targets node (to incubation) is not in" +
                        " " +
                        "commitOld");
            }
            setTarget(committedTarget.getNid());
            /* Adjust version if needed */
            if (isUpdateVersionOnCommit()) {
                setTargetVersion(committedTarget.getVersion());
            }
        }

        /* Commit state */
        getDocument().field(FIELD_ISCOM, true, OType.BOOLEAN);

        /* Remove unused fields */
        getDocument().removeField(FIELD_UNCOMMITTED);
        getDocument().removeField(FIELD_TARGET_IS_INC);
        getDocument().removeField(FIELD_UPDATE_VESION_ON_COMMIT);
    }

    public static void setupDb(BaseModule baseModule) {
        final OSchema schema = baseModule.getDb().getMetadata().getSchema();
        if (!schema.existsClass(RelationWrapper.CLASS_NAME)) {
            final OClass clazz = schema.createClass(RelationWrapper.CLASS_NAME);
            clazz.createProperty(RelationWrapper.FIELD_UNCOMMITTED, OType.BOOLEAN);
            clazz.createProperty(RelationWrapper.FIELD_ISCOM, OType.BOOLEAN);
            clazz.createProperty(RelationWrapper.FIELD_LABEL_NAME, OType.SHORT);
            clazz.createProperty(RelationWrapper.FIELD_LABEL_INDEX, OType.LONG);
            clazz.createProperty(RelationWrapper.FIELD_SRC, OType.STRING);
            clazz.createProperty(RelationWrapper.FIELD_TARGET, OType.STRING);

            clazz.createIndex(INDEX_INC_NODES, OClass.INDEX_TYPE.NOTUNIQUE,
                    RelationWrapper.FIELD_UNCOMMITTED, RelationWrapper.FIELD_SRC);

            clazz.createIndex(INDEX_NODES_BY_LABEL, OClass.INDEX_TYPE.NOTUNIQUE,
                    RelationWrapper.FIELD_ISCOM, RelationWrapper.FIELD_SRC,
                    RelationWrapper.FIELD_LABEL_NAME, RelationWrapper.FIELD_LABEL_INDEX);

            clazz.createIndex(INDEX_IN_EDGES, OClass.INDEX_TYPE.NOTUNIQUE,
                    RelationWrapper.FIELD_ISCOM, RelationWrapper.FIELD_TARGET,
                    RelationWrapper.FIELD_LABEL_NAME, RelationWrapper.FIELD_LABEL_INDEX);

            schema.save();
        }
    }
}
