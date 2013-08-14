package com.dcrux.buran.refimpl.modules.newRelations;

import com.dcrux.buran.common.Nid;
import com.dcrux.buran.common.classes.ClassId;
import com.dcrux.buran.refimpl.modules.BaseModule;
import com.dcrux.buran.refimpl.modules.common.DocumentWrapper;
import com.dcrux.buran.refimpl.modules.nodeWrapper.FieldIndexAndClassId;
import com.orientechnologies.orient.core.id.ORID;
import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.metadata.schema.OSchema;
import com.orientechnologies.orient.core.metadata.schema.OType;
import com.orientechnologies.orient.core.record.impl.ODocument;

/**
 * Buran.
 *
 * @author: ${USER} Date: 04.08.13 Time: 20:59
 */
public class NewRelationsWrapper extends DocumentWrapper {
    public NewRelationsWrapper(ODocument document) {
        super(document);
    }

    public static final String CLASS_NAME = "newRel";

    public static final String FIELD_BUG_FIX = "X";
    public static final String FIELD_SOURCE_NID = "snid";
    public static final String FIELD_SOURCE_CID = "scid";
    public static final String FIELD_SOURCE_FIDX = "sfidx";
    public static final String FIELD_SOURCE_IS_PRIMARY_CLASS_FIELD = "spcf";
    public static final String FIELD_TARGET_CID = "tcid";
    public static final String FIELD_TARGET_ORID = "torid";
    public static final String FIELD_TARGET_VERSIONED = "tver";

    public static final String IDX_SOURCE_NID_AND_CLASSID_AND_FIELD_INDEX = "srcNidFieldIndex";
    public static final String IDX_TARGET_AND_SRC_CLASSID_AND_FIELD_INDEX = "targetClsIdFieldIndex";

    public static NewRelationsWrapper c(ORID sourceNid, FieldIndexAndClassId sourceFieldIndex,
            ClassId targetClassId, ORID target, boolean targetIsVersioned) {
        ODocument oDocument = new ODocument(CLASS_NAME);
        oDocument.field(FIELD_BUG_FIX, FIELD_BUG_FIX, OType.STRING);
        oDocument.field(FIELD_SOURCE_NID, sourceNid, OType.LINK);
        oDocument.field(FIELD_SOURCE_CID, sourceFieldIndex.getClassId().getId(), OType.LONG);
        oDocument.field(FIELD_SOURCE_FIDX, sourceFieldIndex.getIndex().getIndex(), OType.INTEGER);
        oDocument.field(FIELD_SOURCE_IS_PRIMARY_CLASS_FIELD, sourceFieldIndex.isPrimaryClass(),
                OType.BOOLEAN);
        oDocument.field(FIELD_TARGET_CID, targetClassId.getId(), OType.LONG);
        oDocument.field(FIELD_TARGET_ORID, target, OType.LINK);
        oDocument.field(FIELD_TARGET_VERSIONED, targetIsVersioned, OType.BOOLEAN);
        return new NewRelationsWrapper(oDocument);
    }

    public Nid getSource() {
        final ORID source = getDocument().field(FIELD_SOURCE_NID, OType.LINK);
        return new Nid(source.getIdentity().toString());
    }

    public static void setupDb(BaseModule baseModule) {
        final OSchema schema = baseModule.getDb().getMetadata().getSchema();
        if (!schema.existsClass(CLASS_NAME)) {
            final OClass clazz = schema.createClass(CLASS_NAME);
            clazz.createProperty(FIELD_BUG_FIX, OType.STRING);
            clazz.createProperty(FIELD_SOURCE_NID, OType.LINK);
            clazz.createProperty(FIELD_SOURCE_CID, OType.LONG);
            clazz.createProperty(FIELD_SOURCE_FIDX, OType.INTEGER);
            clazz.createProperty(FIELD_SOURCE_IS_PRIMARY_CLASS_FIELD, OType.BOOLEAN);
            clazz.createProperty(FIELD_TARGET_CID, OType.LONG);
            clazz.createProperty(FIELD_TARGET_ORID, OType.LINK);
            clazz.createProperty(FIELD_TARGET_VERSIONED, OType.BOOLEAN);

            /* Source NID and Field Index */
            clazz.createIndex(IDX_SOURCE_NID_AND_CLASSID_AND_FIELD_INDEX,
                    OClass.INDEX_TYPE.NOTUNIQUE, FIELD_BUG_FIX, FIELD_SOURCE_NID, FIELD_SOURCE_CID,
                    FIELD_SOURCE_FIDX);

            /* Target NID, src classId, src fieldIndex */
            clazz.createIndex(IDX_TARGET_AND_SRC_CLASSID_AND_FIELD_INDEX,
                    OClass.INDEX_TYPE.NOTUNIQUE, FIELD_BUG_FIX, FIELD_TARGET_ORID, FIELD_SOURCE_CID,
                    FIELD_SOURCE_FIDX);

            schema.save();
        }
    }
}
