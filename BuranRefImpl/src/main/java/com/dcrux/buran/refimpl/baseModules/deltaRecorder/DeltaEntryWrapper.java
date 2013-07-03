package com.dcrux.buran.refimpl.baseModules.deltaRecorder;

import com.dcrux.buran.refimpl.baseModules.BaseModule;
import com.dcrux.buran.refimpl.baseModules.common.DocumentWrapper;
import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.metadata.schema.OSchema;
import com.orientechnologies.orient.core.metadata.schema.OType;
import com.orientechnologies.orient.core.record.impl.ODocument;

/**
 * Buran.
 *
 * @author: ${USER} Date: 03.07.13 Time: 13:46
 */
public class DeltaEntryWrapper extends DocumentWrapper {

    public static final String CLASS_NAME = "DeltaEntry";

    public static final String FIELD_SERIALIZED_DATA = "serData";

    public DeltaEntryWrapper(ODocument document) {
        super(document);
    }

    public static DeltaEntryWrapper create(byte[] serData) {
        ODocument doc = new ODocument(CLASS_NAME);
        doc.field(FIELD_SERIALIZED_DATA, serData, OType.BINARY);
        return new DeltaEntryWrapper(doc);
    }

    public byte[] getSerData() {
        return getDocument().field(FIELD_SERIALIZED_DATA, OType.BINARY);
    }

    public static void setupDb(BaseModule bm) {
        final OSchema schema = bm.getDb().getMetadata().getSchema();
        if (!schema.existsClass(CLASS_NAME)) {
            OClass clazz = schema.createClass(CLASS_NAME);
            clazz.createProperty(FIELD_SERIALIZED_DATA, OType.BINARY);
        }
    }
}
