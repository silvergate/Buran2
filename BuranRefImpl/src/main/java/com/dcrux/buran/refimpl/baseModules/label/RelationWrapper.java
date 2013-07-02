package com.dcrux.buran.refimpl.baseModules.label;

import com.dcrux.buran.common.labels.ClassLabelName;
import com.dcrux.buran.common.labels.LabelIndex;
import com.dcrux.buran.refimpl.baseModules.common.ONid;
import com.dcrux.buran.refimpl.baseModules.fields.DocFields;
import com.orientechnologies.orient.core.id.ORID;
import com.orientechnologies.orient.core.metadata.schema.OType;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.sun.istack.internal.Nullable;

/**
 * Buran.
 *
 * @author: ${USER} Date: 01.07.13 Time: 02:21
 */
public class RelationWrapper {
    private final ODocument document;

    public RelationWrapper(ODocument document) {
        this.document = document;
    }

    public static final String CLASS_NAME = "ClassRelation";

    public static final String FIELD_SRC_IS_INC = "sinc";
    public static final String FIELD_TARGET_IS_INC = "tinc";
    public static final String FIELD_SRC = "src";
    public static final String FIELD_TARGET = "target";
    public static final String FIELD_LABEL_NAME = "labelName";
    public static final String FIELD_LABEL_INDEX = "labelIndex";

    public static RelationWrapper createInc(ONid source, boolean sourceIsInc, ONid target,
            boolean targetIsInc, ClassLabelName labelName, LabelIndex index) {
        ODocument doc = new ODocument(CLASS_NAME);
        doc.field(DocFields.CR_FIELD_LIVE, false, OType.BOOLEAN);
        if (sourceIsInc) {
            doc.field(FIELD_SRC_IS_INC, true, OType.BOOLEAN);
        }
        if (targetIsInc) {
            doc.field(FIELD_TARGET_IS_INC, true, OType.BOOLEAN);
        }
        doc.field(FIELD_LABEL_NAME, labelName.getIndex(), OType.SHORT);
        doc.field(FIELD_LABEL_INDEX, index.getIndex(), OType.LONG);
        RelationWrapper relationWrapper = new RelationWrapper(doc);
        relationWrapper.setSource(source);
        relationWrapper.setTarget(target);
        return relationWrapper;
    }

    public ODocument getDocument() {
        return document;
    }

    protected void setTarget(ONid target) {
        getDocument().field(FIELD_TARGET, target.getRecordId(), OType.LINK);
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

    public ONid getTarget() {
        final ORID target = getDocument().field(FIELD_TARGET, OType.LINK);
        if (target == null) {
            return null;
        }
        return new ONid(target);
    }

    public boolean isLive() {
        //TODO: Es reicht eigentlich ein check auf "field src is inc"
        final Boolean value = getDocument().field(DocFields.CR_FIELD_LIVE, OType.BOOLEAN);
        return (value == null) || (value);
    }

    public void goLive(ONid source, ONid target) {
        setSource(source);
        setTarget(target);
        getDocument().removeField(DocFields.CR_FIELD_LIVE);
        getDocument().removeField(FIELD_SRC_IS_INC);
        getDocument().removeField(FIELD_TARGET_IS_INC);
    }
}
