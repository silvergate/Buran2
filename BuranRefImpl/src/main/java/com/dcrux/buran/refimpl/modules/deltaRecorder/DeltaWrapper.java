package com.dcrux.buran.refimpl.modules.deltaRecorder;

import com.dcrux.buran.refimpl.modules.BaseModule;
import com.dcrux.buran.refimpl.modules.common.DocumentWrapper;
import com.dcrux.buran.refimpl.modules.common.ONid;
import com.orientechnologies.orient.core.index.OIndex;
import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.metadata.schema.OSchema;
import com.orientechnologies.orient.core.metadata.schema.OType;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.sun.istack.internal.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Buran.
 *
 * @author: ${USER} Date: 03.07.13 Time: 13:46
 */
public class DeltaWrapper extends DocumentWrapper {

    public static final String CLASS_NAME = "Delta";

    public static final String FIELD_INC_NODE_ID = "inid";
    public static final String FIELD_ENTRY_LIST = "el";

    public static final String INDEX_INC_NODE_ID = "indexinid";

    public DeltaWrapper(ODocument document) {
        super(document);
    }

    public static DeltaWrapper create(ONid incubationNode) {
        ODocument doc = new ODocument(CLASS_NAME);
        doc.field(FIELD_INC_NODE_ID, incubationNode.getAsString(), OType.STRING);
        return new DeltaWrapper(doc);
    }

    public void addEntry(DeltaEntryWrapper deltaEntryWrapper) {
        List<Object> entries = getDocument().field(FIELD_ENTRY_LIST, OType.LINKLIST);
        if (entries == null) {
            entries = new ArrayList<>();
        }
        entries.add(deltaEntryWrapper.getNid().getRecordId());
        getDocument().field(FIELD_ENTRY_LIST, entries, OType.LINKLIST);
    }

    @Nullable
    public List<ODocument> getEntries() {
        return getDocument().field(FIELD_ENTRY_LIST, OType.LINKLIST);
    }

    public static void setupDb(BaseModule bm) {
        final OSchema schema = bm.getDb().getMetadata().getSchema();
        if (!schema.existsClass(CLASS_NAME)) {
            OClass clazz = schema.createClass(CLASS_NAME);
            clazz.createProperty(FIELD_INC_NODE_ID, OType.STRING);
            clazz.createProperty(FIELD_ENTRY_LIST, OType.LINKLIST);

            /* Index */
            OIndex index = clazz.createIndex(INDEX_INC_NODE_ID, OClass.INDEX_TYPE.UNIQUE,
                    FIELD_INC_NODE_ID);
            schema.save();
        }
    }


}
