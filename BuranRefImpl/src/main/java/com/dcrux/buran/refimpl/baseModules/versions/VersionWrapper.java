package com.dcrux.buran.refimpl.baseModules.versions;

import com.dcrux.buran.common.Version;
import com.dcrux.buran.refimpl.baseModules.BaseModule;
import com.dcrux.buran.refimpl.baseModules.common.DocumentWrapper;
import com.dcrux.buran.refimpl.baseModules.common.ONid;
import com.dcrux.buran.refimpl.baseModules.common.ONidVer;
import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.metadata.schema.OSchema;
import com.orientechnologies.orient.core.metadata.schema.OType;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.sun.istack.internal.Nullable;

/**
 * Buran.
 *
 * @author: ${USER} Date: 08.07.13 Time: 17:38
 */
public class VersionWrapper extends DocumentWrapper {

    public static final String ORIENT_CLASS = "Versions";

    public static final String FIELD_VERSION = "ver";
    public static final String FIELD_NODE_OID = "noid";

    public static final String INDEX_NOID_VERSION = "noidVersion";

    public VersionWrapper(ODocument document) {
        super(document);
    }

    public static VersionWrapper createNew(ONid onid, Version version) {
        ODocument doc = new ODocument(ORIENT_CLASS);
        doc.field(FIELD_VERSION, version.getVersion(), OType.LONG);
        doc.field(FIELD_NODE_OID, onid.getAsString(), OType.STRING);
        return new VersionWrapper(doc);
    }

    @Nullable
    public Version getVersion() {
        final Long version = getDocument().field(FIELD_VERSION, OType.LONG);
        if (version != null) {
            return new Version(version);
        }
        return null;
    }

    @Nullable
    public ONid getONid() {
        final String target = getDocument().field(FIELD_NODE_OID, OType.STRING);
        if (target != null) {
            return ONid.fromString(target);
        }
        return null;
    }

    public ONidVer getONidVer() {
        return new ONidVer(getDocument().getIdentity());
    }

    public void deactivate() {
        getDocument().removeField(FIELD_VERSION);
        getDocument().removeField(FIELD_NODE_OID);
    }

    public static void setupDb(BaseModule baseModule) {
        final OSchema schema = baseModule.getDb().getMetadata().getSchema();
        if (!schema.existsClass(ORIENT_CLASS)) {
            final OClass clazz = schema.createClass(ORIENT_CLASS);
            clazz.createProperty(FIELD_NODE_OID, OType.STRING);
            clazz.createProperty(FIELD_VERSION, OType.LONG);

            clazz.createIndex(INDEX_NOID_VERSION, OClass.INDEX_TYPE.UNIQUE, FIELD_NODE_OID,
                    FIELD_VERSION);
            schema.save();
        }
    }


}
