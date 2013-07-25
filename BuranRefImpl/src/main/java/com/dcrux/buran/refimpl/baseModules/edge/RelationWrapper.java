package com.dcrux.buran.refimpl.baseModules.edge;

import com.dcrux.buran.common.*;
import com.dcrux.buran.common.classes.ClassId;
import com.dcrux.buran.common.edges.*;
import com.dcrux.buran.common.edges.targets.EdgeTarget;
import com.dcrux.buran.common.edges.targets.EdgeTargetExt;
import com.dcrux.buran.common.edges.targets.EdgeTargetInc;
import com.dcrux.buran.common.exceptions.IncNodeNotFound;
import com.dcrux.buran.common.exceptions.NodeNotFoundException;
import com.dcrux.buran.refimpl.baseModules.BaseModule;
import com.dcrux.buran.refimpl.baseModules.common.IfaceUtils;
import com.dcrux.buran.refimpl.baseModules.common.ONid;
import com.dcrux.buran.refimpl.baseModules.nodeWrapper.IncubationNode;
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

    public static enum TargetType2 {
        incVersioned(0),
        incUnversioned(1),
        versioned(2),
        unversioned(3),
        extVersioned(4),
        extUnversioned(5);

        private int value;

        private TargetType2(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static TargetType2 fromInt(int value) {
            switch (value) {
                case 0:
                    return incVersioned;
                case 1:
                    return incUnversioned;
                case 2:
                    return versioned;
                case 3:
                    return unversioned;
                case 4:
                    return extVersioned;
                case 5:
                    return extUnversioned;
                default:
                    throw new IllegalArgumentException("fromInt");
            }
        }
    }

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
    public static final String INDEX_IN_EDGES_WITH_SRC_NODE = "inEdgesIndexSrcNode";
    public static final String INDEX_IN_EDGES_WITH_SRC_CLASS = "inEdgesIndexSrcClass";
    public static final String INDEX_IN_EDGES_WITH_PUB_LABEL = "inEdgesIndexPubLabel";

    public static final String CLASS_NAME = "ClassRelation";

    /* Source */
    public static final String FIELD_SRC = "src";
    public static final String FIELD_SRC_CLASS = "srcCls";

    /* Label and index */
    public static final String FIELD_LABEL_NAME = "labelName";
    public static final String FIELD_LABEL_NAME_PUB = "plabelName";
    public static final String FIELD_LABEL_INDEX = "labelIndex";

    /* Target adjustment */
    public static final String FIELD_UNCOMMITTED = "isuncom";
    public static final String FIELD_ISCOM = "iscom";

    /* Target */
    public static final String FIELD_TARGET_TYPE = "ttype";
    public static final String FIELD_TARGET_IS_INC = "tinc";
    public static final String FIELD_TARGET = "target";
    public static final String FIELD_TARGET_CLASS = "tclass";
    @Deprecated
    public static final String FIELD_TARGET_VERSION = "tver";
    public static final String FIELD_UPDATE_VESION_ON_COMMIT = "uvoc";
    public static final String FIELD_TARGET_USERID = "tuid";

    public static RelationWrapper from(UserId sender, ONid source, ClassId sourceClass,
            ILabelName labelName, LabelIndex index, IEdgeTargetInc targetInc, BaseModule baseModule)
            throws NodeNotFoundException, IncNodeNotFound {
        final ONid target;
        @Deprecated
        final @Nullable Version targetVersion;
        final @Nullable UserId targetUserId;
        final TargetType targetType;
        final TargetType2 targetType2;
        final ClassId targetClass;

        if (targetInc instanceof EdgeTarget) {
            final EdgeTarget labelTarget = (EdgeTarget) targetInc;
            final INidOrNidVer nidOrNidVer = labelTarget.getTargetNid();
            if (nidOrNidVer instanceof Nid) {
                final Nid nid = (Nid) nidOrNidVer;
                final LiveNode liveNode = baseModule.getDataFetchModule().getNode(nid);
                targetType2 = TargetType2.unversioned;
                targetClass = liveNode.getClassId();
            } else if (nidOrNidVer instanceof NidVer) {
                final NidVer nidVer = (NidVer) nidOrNidVer;
                final LiveNode liveNode = baseModule.getDataFetchModule().getNode(nidVer);
                targetType2 = TargetType2.unversioned;
                targetClass = liveNode.getClassId();
            } else {
                throw new IllegalArgumentException("Unknown INidOrNidVer type");
            }
            target = baseModule.getDataFetchModule().toOnid(labelTarget.getTargetNid());
            targetUserId = null;
            targetType = TargetType.liveTarget;
            targetVersion = null;
        } else if (targetInc instanceof EdgeTargetExt) {
            EdgeTargetExt labelTargetExt = (EdgeTargetExt) targetInc;
            targetClass = labelTargetExt.getTargetClassId();
            final IExtNidOrNidVer extNid = (labelTargetExt.getExtNidOrNidVer());
            if (extNid instanceof ExtNid) {
                targetType2 = TargetType2.extUnversioned;
                target = IfaceUtils.getONid(((ExtNid) extNid).getNid());
                targetUserId = ((ExtNid) extNid).getAccount();
            } else if (extNid instanceof ExtNidVer) {
                targetType2 = TargetType2.extVersioned;
                target = ONid.fromString(((ExtNidVer) extNid).getNidVer().getAsString());
                targetUserId = ((ExtNidVer) extNid).getAccount();
            } else {
                throw new IllegalArgumentException("Unknown extNid type");
            }
            targetVersion = null;
            targetType = TargetType.liveTarget;
        } else if (targetInc instanceof EdgeTargetInc) {
            EdgeTargetInc labelTargeInc = (EdgeTargetInc) targetInc;

            target = IfaceUtils.getOincNid(labelTargeInc.getTargetNid());
            targetVersion = null;
            targetUserId = null;
            if (labelTargeInc.isVersioned()) {
                targetType = TargetType.incubationVersioned;
                targetType2 = TargetType2.incVersioned;
            } else {
                targetType = TargetType.incubationUnversioned;
                targetType2 = TargetType2.incUnversioned;
            }
            final IncubationNode incNode = baseModule.getIncubationModule()
                    .getIncNodeReq(sender, labelTargeInc.getTargetNid());
            targetClass = incNode.getClassId();
        } else {
            throw new IllegalArgumentException("Unknown target type");
        }

        return createInc(source, sourceClass, targetType2, target, targetVersion, targetUserId,
                targetClass, targetType, labelName, index);
    }

    private static RelationWrapper createInc(ONid source, ClassId srcClass, TargetType2 targetType2,
            ONid target, @Nullable Version targetVersion, @Nullable UserId targetUserId,
            ClassId targetClass, TargetType targetType, ILabelName labelName, LabelIndex index) {
        ODocument doc = new ODocument(CLASS_NAME);

        doc.field(FIELD_SRC_CLASS, srcClass.getId(), OType.LONG);
        doc.field(FIELD_LABEL_INDEX, index.getIndex(), OType.LONG);
        doc.field(FIELD_TARGET_CLASS, targetClass.getId(), OType.LONG);
        doc.field(FIELD_TARGET_TYPE, targetType2.getValue(), OType.INTEGER);

        if (labelName instanceof ClassLabelName) {
            doc.field(FIELD_LABEL_NAME, ((ClassLabelName) labelName).getIndex(), OType.SHORT);
        } else if (labelName instanceof PublicLabelName) {
            doc.field(FIELD_LABEL_NAME_PUB, ((PublicLabelName) labelName).getName(), OType.STRING);
        } else {
            throw new IllegalArgumentException("Unknown label name type");
        }

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

    public TargetType2 getTargetType() {
        final int type = getDocument().field(FIELD_TARGET_TYPE, OType.INTEGER);
        return TargetType2.fromInt(type);
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

    public ClassId getTargetClass() {
        final Long tclass = getDocument().field(FIELD_TARGET_CLASS, OType.LONG);
        return new ClassId(tclass);
    }

    public void setTargetVersion(Version version) {
        getDocument().field(FIELD_TARGET_VERSION, version.getVersion(), OType.LONG);
    }

    public boolean isUpdateVersionOnCommit() {
        Boolean updateOnCommit = getDocument().field(FIELD_UPDATE_VESION_ON_COMMIT, OType.BOOLEAN);
        return (updateOnCommit != null) && (updateOnCommit);
    }

    public void goLive(@Nullable LiveNode committedTarget, NidVer commitedTargetNidVer) {
        /* Adjust target */
        if (isTargetIsInc()) {
            if (committedTarget == null) {
                throw new IllegalStateException("Label targets node (to incubation) is not in" +
                        " " +
                        "commitOld");
            }

            final TargetType2 oldType = getTargetType();
            final TargetType2 newType;
            if (oldType == TargetType2.incVersioned) {
                getDocument().field(FIELD_TARGET, commitedTargetNidVer.getAsString(), OType.STRING);
                newType = TargetType2.versioned;
            } else if (oldType == TargetType2.incUnversioned) {
                getDocument()
                        .field(FIELD_TARGET, committedTarget.getNid().getAsString(), OType.STRING);
                newType = TargetType2.unversioned;
            } else {
                throw new IllegalArgumentException("Wrong target type");
            }

            getDocument().field(FIELD_TARGET_TYPE, newType.getValue(), OType.INTEGER);
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
            clazz.createProperty(RelationWrapper.FIELD_LABEL_NAME_PUB, OType.STRING);
            clazz.createProperty(RelationWrapper.FIELD_LABEL_INDEX, OType.LONG);
            clazz.createProperty(RelationWrapper.FIELD_SRC, OType.STRING);
            clazz.createProperty(RelationWrapper.FIELD_SRC_CLASS, OType.LONG);
            clazz.createProperty(RelationWrapper.FIELD_TARGET, OType.STRING);
            clazz.createProperty(RelationWrapper.FIELD_TARGET_TYPE, OType.INTEGER);
            clazz.createProperty(RelationWrapper.FIELD_TARGET_CLASS, OType.LONG);

            clazz.createIndex(INDEX_INC_NODES, OClass.INDEX_TYPE.NOTUNIQUE,
                    RelationWrapper.FIELD_UNCOMMITTED, RelationWrapper.FIELD_SRC);

            clazz.createIndex(INDEX_NODES_BY_LABEL, OClass.INDEX_TYPE.NOTUNIQUE,
                    RelationWrapper.FIELD_ISCOM, RelationWrapper.FIELD_SRC,
                    RelationWrapper.FIELD_LABEL_NAME, RelationWrapper.FIELD_LABEL_INDEX);

            /* Index source node given */
            clazz.createIndex(INDEX_IN_EDGES_WITH_SRC_NODE, OClass.INDEX_TYPE.NOTUNIQUE,
                    RelationWrapper.FIELD_ISCOM, RelationWrapper.FIELD_TARGET,
                    RelationWrapper.FIELD_LABEL_NAME, RelationWrapper.FIELD_TARGET_TYPE,
                    RelationWrapper.FIELD_SRC, RelationWrapper.FIELD_LABEL_INDEX);

            /* Index source class given */
            clazz.createIndex(INDEX_IN_EDGES_WITH_SRC_CLASS, OClass.INDEX_TYPE.NOTUNIQUE,
                    RelationWrapper.FIELD_ISCOM, RelationWrapper.FIELD_TARGET,
                    RelationWrapper.FIELD_LABEL_NAME, RelationWrapper.FIELD_TARGET_TYPE,
                    RelationWrapper.FIELD_SRC_CLASS, RelationWrapper.FIELD_LABEL_INDEX);

            /* Index with public label */
            clazz.createIndex(INDEX_IN_EDGES_WITH_PUB_LABEL, OClass.INDEX_TYPE.NOTUNIQUE,
                    RelationWrapper.FIELD_ISCOM, RelationWrapper.FIELD_TARGET,
                    RelationWrapper.FIELD_LABEL_NAME_PUB, RelationWrapper.FIELD_TARGET_TYPE,
                    RelationWrapper.FIELD_LABEL_INDEX);

            schema.save();
        }
    }
}
