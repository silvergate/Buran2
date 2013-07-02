package com.dcrux.buran.refimpl.baseModules.label;

import com.dcrux.buran.common.UserId;
import com.dcrux.buran.common.Version;
import com.dcrux.buran.common.labels.ClassLabelName;
import com.dcrux.buran.common.labels.ILabelTargetInc;
import com.dcrux.buran.common.labels.LabelIndex;
import com.dcrux.buran.common.labels.targets.LabelTarget;
import com.dcrux.buran.common.labels.targets.LabelTargetExt;
import com.dcrux.buran.common.labels.targets.LabelTargetInc;
import com.dcrux.buran.refimpl.baseModules.common.IfaceUtils;
import com.dcrux.buran.refimpl.baseModules.common.ONid;
import com.dcrux.buran.refimpl.baseModules.nodeWrapper.LiveNode;
import com.orientechnologies.orient.core.id.ORecordId;
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

    public static final String CLASS_NAME = "ClassRelation";

    /* Source */
    public static final String FIELD_SRC = "src";
    public static final String FIELD_SRC_IS_INC = "sinc";

    /* Label and index */
    public static final String FIELD_LABEL_NAME = "labelName";
    public static final String FIELD_LABEL_INDEX = "labelIndex";

    /* Target */
    public static final String FIELD_TARGET_IS_INC = "tinc";
    public static final String FIELD_TARGET = "target";
    public static final String FIELD_TARGET_VERSION = "tver";
    public static final String FIELD_UPDATE_VESION_ON_COMMIT = "uvoc";
    public static final String FIELD_TARGET_USERID = "tuid";

    public static RelationWrapper from(ONid source, ClassLabelName labelName, LabelIndex index,
            ILabelTargetInc targetInc) {
        final ONid target;
        final @Nullable Version targetVersion;
        final @Nullable UserId targetUserId;
        final TargetType targetType;

        if (targetInc instanceof LabelTarget) {
            final LabelTarget labelTarget = (LabelTarget) targetInc;
            target = IfaceUtils.getONid(labelTarget.getTargetNid());
            targetVersion = labelTarget.getVersion().orNull();
            targetUserId = null;
            targetType = TargetType.liveTarget;
        } else if (targetInc instanceof LabelTargetExt) {
            LabelTargetExt labelTargetExt = (LabelTargetExt) targetInc;
            target = IfaceUtils.getONid(labelTargetExt.getTargetNid());
            targetVersion = labelTargetExt.getVersion().orNull();
            targetUserId = labelTargetExt.getUserId();
            targetType = TargetType.liveTarget;
        } else if (targetInc instanceof LabelTargetInc) {
            LabelTargetInc labelTargeInc = (LabelTargetInc) targetInc;

            target = IfaceUtils.getOincNid(labelTargeInc.getTargetNid());
            targetVersion = null;
            targetUserId = null;
            if (labelTargeInc.isVersioned()) targetType = TargetType.incubationVersioned;
            else targetType = TargetType.incubationUnversioned;
        } else {
            throw new IllegalArgumentException("Unknown target type");
        }

        return createInc(source, target, targetVersion, targetUserId, targetType, labelName, index);
    }

    private static RelationWrapper createInc(ONid source, ONid target,
            @Nullable Version targetVersion, @Nullable UserId targetUserId, TargetType targetType,
            ClassLabelName labelName, LabelIndex index) {
        ODocument doc = new ODocument(CLASS_NAME);
        doc.field(FIELD_SRC_IS_INC, true, OType.BOOLEAN);

        doc.field(FIELD_LABEL_NAME, labelName.getIndex(), OType.SHORT);
        doc.field(FIELD_LABEL_INDEX, index.getIndex(), OType.LONG);
        RelationWrapper relationWrapper = new RelationWrapper(doc);
        relationWrapper.setSource(source);

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

    public void goLive(ONid source, @Nullable LiveNode committedTarget) {
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

        /* Adjust source */
        setSource(source);

        /* Remove unused fields */
        getDocument().removeField(FIELD_SRC_IS_INC);
        getDocument().removeField(FIELD_TARGET_IS_INC);
        getDocument().removeField(FIELD_UPDATE_VESION_ON_COMMIT);
    }
}
