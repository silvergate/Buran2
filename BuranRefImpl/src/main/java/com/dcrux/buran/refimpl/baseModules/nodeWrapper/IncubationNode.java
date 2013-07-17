package com.dcrux.buran.refimpl.baseModules.nodeWrapper;

import com.dcrux.buran.common.UserId;
import com.dcrux.buran.common.Version;
import com.dcrux.buran.common.classes.ClassId;
import com.dcrux.buran.refimpl.baseModules.common.ONidVer;
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

    /* Incubation Node */
    public static final String INC_FIELD_ADD_TIME = "added";
    public static final String INC_FIELD_UPDATE_VERSION = "uversion";
    public static final String INC_FIELD_UPDATE_NODE = "unode";

    public IncubationNode(ODocument document) {
        super(document);
    }

    public static IncubationNode createNew(ClassId classId, final long addTime, UserId sender) {
        final ODocument document = new ODocument(ClassNameUtils.generateNodeClasName(classId));
        final IncubationNode incNode = new IncubationNode(document);
        incNode.getDocument().field(CommonNode.FIELD_COMMON_LIVE, false, OType.BOOLEAN);
        incNode.setAddTime(addTime);
        incNode.setSender(sender);
        return incNode;
    }

    public static IncubationNode createUpdate(ClassId classId, final long addTime, UserId sender,
            ONidVer update) {
        final ODocument document = new ODocument(ClassNameUtils.generateNodeClasName(classId));
        final IncubationNode incNode = new IncubationNode(document);
        incNode.getDocument().field(CommonNode.FIELD_COMMON_LIVE, false, OType.BOOLEAN);
        incNode.setAddTime(addTime);
        incNode.setSender(sender);
        incNode.setUpdateNid(update);
        return incNode;
    }

    @Deprecated
    public void setUpdateVersion(Version version) {
        getDocument().field(INC_FIELD_UPDATE_VERSION, version.getVersion(), OType.LONG);
    }

    @Deprecated
    @Nullable
    public Version getUpdateVersion() {
        final Long version = getDocument().field(INC_FIELD_UPDATE_VERSION, OType.LONG);
        if (version == null) {
            return null;
        }
        return new Version(version);
    }

    public void setUpdateNid(ONidVer onid) {
        getDocument().field(INC_FIELD_UPDATE_NODE, onid.getoIdentifiable(), OType.LINK);
    }

    @Nullable
    public ONidVer getUpdateNid() {
        final ORecordId recordId = getDocument().field(INC_FIELD_UPDATE_NODE, OType.LINK);
        if (recordId == null) {
            return null;
        }
        return new ONidVer(recordId);
    }

    public boolean isUpdate() {
        ONidVer updateNid = getUpdateNid();
        return (updateNid != null);
    }

    public void goLive() {
        getDocument().removeField(INC_FIELD_ADD_TIME);
        getDocument().removeField(INC_FIELD_UPDATE_VERSION);
        getDocument().removeField(INC_FIELD_UPDATE_NODE);
        getDocument().removeField(FIELD_COMMON_LIVE);
    }

    public void setAddTime(final long time) {
        getDocument().field(INC_FIELD_ADD_TIME, time, OType.LONG);
    }

}
