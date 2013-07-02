package com.dcrux.buran.refimpl.baseModules.nodeWrapper;

import com.dcrux.buran.common.INid;
import com.dcrux.buran.common.NidVer;
import com.dcrux.buran.common.Version;
import com.dcrux.buran.refimpl.baseModules.fields.DocFields;
import com.orientechnologies.orient.core.metadata.schema.OType;
import com.orientechnologies.orient.core.record.impl.ODocument;

/**
 * Buran.
 *
 * @author: ${USER} Date: 29.06.13 Time: 13:46
 */
public class LiveNode extends CommonNode {

    public LiveNode(ODocument document) {
        super(document);
    }

    public void setCommitTime(final long time) {
        getDocument().field(DocFields.FIELD_COMMIT_TIME, time, OType.LONG);
    }

    public Version getVersion() {
        final long version = getDocument().field(DocFields.FIELD_VERSION, OType.LONG);
        return new Version(version);
    }

    public void setVersion(Version version) {
        getDocument().field(DocFields.FIELD_VERSION, version.getVersion(), OType.LONG);
    }

    public void incVersion() {
        final long version = getDocument().field(DocFields.FIELD_VERSION, OType.LONG);
        getDocument().field(DocFields.FIELD_VERSION, version + 1, OType.LONG);
    }

    public NidVer getNidVer() {
        final INid nid = getNid();
        final Version version = getVersion();
        return new NidVer(nid, version);
    }
}
