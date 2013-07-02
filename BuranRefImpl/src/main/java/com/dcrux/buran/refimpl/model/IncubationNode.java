package com.dcrux.buran.refimpl.model;

import com.dcrux.buran.ClassId;
import com.dcrux.buran.UserId;
import com.dcrux.buran.Version;
import com.dcrux.buran.refimpl.baseModules.fields.DocFields;
import com.dcrux.buran.refimpl.dao.ClassNameUtils;
import com.orientechnologies.orient.core.id.ORecordId;
import com.orientechnologies.orient.core.metadata.schema.OType;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.sun.istack.internal.Nullable;

/**
 * Buran.
 *
 * @author: ${USER} Date: 29.06.13 Time: 13:46
 */
public class IncubationNode extends CommonNode {

    public IncubationNode(ODocument document) {
        super(document);
    }

    public static IncubationNode createNew(ClassId classId, final long addTime, UserId sender) {
        final ODocument document = new ODocument(ClassNameUtils.generateNodeClasName(classId));
        final IncubationNode incNode = new IncubationNode(document);
        incNode.getDocument().field(DocFields.FIELD_COMMON_LIVE, false, OType.BOOLEAN);
        incNode.setAddTime(addTime);
        incNode.setSender(sender);
        return incNode;
    }

    public static IncubationNode createUpdate(ClassId classId, final long addTime, UserId sender,
            ONid update, Version updateVersion) {
        final ODocument document = new ODocument(ClassNameUtils.generateNodeClasName(classId));
        final IncubationNode incNode = new IncubationNode(document);
        incNode.getDocument().field(DocFields.FIELD_COMMON_LIVE, false, OType.BOOLEAN);
        incNode.setAddTime(addTime);
        incNode.setSender(sender);
        incNode.setUpdateNid(update);
        incNode.setUpdateVersion(updateVersion);
        return incNode;
    }

    public void setUpdateVersion(Version version) {
        getDocument().field(DocFields.INC_FIELD_UPDATE_VERSION, version.getVersion(), OType.LONG);
    }

    @Nullable
    public Version getUpdateVersion() {
        final Long version = getDocument().field(DocFields.INC_FIELD_UPDATE_VERSION, OType.LONG);
        if (version == null) {
            return null;
        }
        return new Version(version);
    }

    public void setUpdateNid(ONid onid) {
        getDocument().field(DocFields.INC_FIELD_UPDATE_NODE, onid.getRecordId(), OType.LINK);
    }

    @Nullable
    public ONid getUpdateNid() {
        final ORecordId recordId = getDocument().field(DocFields.INC_FIELD_UPDATE_NODE, OType.LINK);
        if (recordId == null) {
            return null;
        }
        return new ONid(recordId);
    }


    public void goLive() {
        getDocument().removeField(DocFields.INC_FIELD_ADD_TIME);
        getDocument().removeField(DocFields.INC_FIELD_UPDATE_VERSION);
        getDocument().removeField(DocFields.INC_FIELD_UPDATE_NODE);
        getDocument().removeField(DocFields.FIELD_COMMON_LIVE);
    }

    public void setAddTime(final long time) {
        getDocument().field(DocFields.INC_FIELD_ADD_TIME, time, OType.LONG);
    }

}
