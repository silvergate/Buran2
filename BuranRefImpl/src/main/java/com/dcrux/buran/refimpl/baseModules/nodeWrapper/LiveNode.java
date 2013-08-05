package com.dcrux.buran.refimpl.baseModules.nodeWrapper;

import com.dcrux.buran.common.Version;
import com.dcrux.buran.refimpl.baseModules.common.INid;
import com.dcrux.buran.refimpl.baseModules.common.NidVerOld;
import com.orientechnologies.orient.core.metadata.schema.OType;
import com.orientechnologies.orient.core.record.impl.ODocument;

/**
 * Buran.
 *
 * @author: ${USER} Date: 29.06.13 Time: 13:46
 */
public class LiveNode extends CommonNode {

    /* Live node */
    public static final String FIELD_COMMIT_TIME = "ct";
    public static final String FIELD_VERSION = "version";

    public LiveNode(ODocument document) {
        super(document);
    }

    public void setCommitTime(final long time) {
        getDocument().field(FIELD_COMMIT_TIME, time, OType.LONG);
    }

    public Version getVersion() {
        final long version = getDocument().field(FIELD_VERSION, OType.LONG);
        return new Version(version);
    }

    public void setVersion(Version version) {
        getDocument().field(FIELD_VERSION, version.getVersion(), OType.LONG);
    }

    public void incVersion() {
        final long version = getDocument().field(FIELD_VERSION, OType.LONG);
        getDocument().field(FIELD_VERSION, version + 1, OType.LONG);
    }

    @Deprecated
    public NidVerOld getNidVer() {
        final INid nid = getNid();
        final Version version = getVersion();
        return new NidVerOld(nid, version);
    }


}
