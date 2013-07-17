package com.dcrux.buran.refimpl.baseModules.versions;

import com.dcrux.buran.common.NidVerOld;
import com.dcrux.buran.common.Version;
import com.dcrux.buran.refimpl.baseModules.BaseModule;
import com.dcrux.buran.refimpl.baseModules.common.Module;
import com.dcrux.buran.refimpl.baseModules.common.ONid;
import com.dcrux.buran.refimpl.baseModules.common.ONidVer;
import com.orientechnologies.orient.core.id.ORID;
import com.orientechnologies.orient.core.index.OIndex;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.sun.istack.internal.Nullable;

import java.util.Collection;

/**
 * Buran.
 *
 * @author: ${USER} Date: 08.07.13 Time: 17:36
 */
public class VersionsModule extends Module<BaseModule> {
    public VersionsModule(BaseModule baseModule) {
        super(baseModule);
    }

    public void setupDb() {
        VersionWrapper.setupDb(getBase());
    }

    public VersionWrapper addNodeVersion(ONid nodeId, Version version) {
        final VersionWrapper versionWrapper = VersionWrapper.createNew(nodeId, version);
        versionWrapper.getDocument().save();
        return versionWrapper;
    }

    @Nullable
    public VersionWrapper getNodeVersion(ONid nodeId, Version version) {
        final OIndex index = getBase().getDbUtils()
                .getIndex(VersionWrapper.ORIENT_CLASS, VersionWrapper.INDEX_NOID_VERSION);
        final Object value =
                index.getDefinition().createValue(nodeId.getAsString(), version.getVersion());
        final Collection entries = index.getValuesBetween(value, value);
        if ((entries == null) || (entries.isEmpty())) {
            return null;
        } else {
            final ODocument doc = getBase().getDb().load((ORID) entries.iterator().next());
            return new VersionWrapper(doc);
        }
    }

    @Nullable
    public NidVerOld getNidVer(ORID versionsRecord) {
        final ODocument doc = getBase().getDb().load(versionsRecord);
        if (doc == null) {
            System.out.println("Node " + versionsRecord + " not found.");
            return null;
        }
        final VersionWrapper versionWrapper = new VersionWrapper(doc);
        return new NidVerOld(versionWrapper.getONid(), versionWrapper.getVersion());
    }

    public VersionWrapper removeNodeVersion(ONidVer oNidVer) {
        final ODocument doc = getBase().getDb().load(oNidVer.getoIdentifiable());
        final VersionWrapper nodeVersion = new VersionWrapper(doc);
        nodeVersion.deactivate();
        nodeVersion.getDocument().save();
        return nodeVersion;
    }

    @Nullable
    public VersionWrapper removeNodeVersion(ONid nodeId, Version version) {
        final VersionWrapper nodeVersion = getNodeVersion(nodeId, version);
        if (nodeVersion != null) {
            nodeVersion.deactivate();
            nodeVersion.getDocument().save();
            return nodeVersion;
        }
        return null;
    }
}
