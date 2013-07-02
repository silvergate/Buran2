package com.dcrux.buran.refimpl.baseModules.classes;

import com.dcrux.buran.common.classes.ClassHashId;
import com.dcrux.buran.common.classes.ClassId;
import com.dcrux.buran.refimpl.baseModules.common.DocumentWrapper;
import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.metadata.schema.OSchema;
import com.orientechnologies.orient.core.metadata.schema.OType;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.sun.istack.internal.Nullable;

/**
 * Buran.
 *
 * @author: ${USER} Date: 02.07.13 Time: 20:46
 */
public class ClassDefWrapper extends DocumentWrapper {

    public static final String CLASS_NAME = "ClassDefinition";
    public static final String FIELD_CID = "cid";
    public static final String FIELD_HASH = "hash";
    public static final String FIELD_DEF = "def";

    public static final String INDEX_HASH = "IndexHash";
    public static final String INDEX_CID = "IndexClassId";

    public ClassDefWrapper(ODocument document) {
        super(document);
    }

    public static ClassDefWrapper c(ClassId classId, ClassHashId classHashId) {
        final ODocument doc = new ODocument(CLASS_NAME);
        doc.field(FIELD_CID, classId.getId(), OType.LONG);
        doc.field(FIELD_HASH, classHashId.getHash(), OType.BINARY);
        return new ClassDefWrapper(doc);
    }

    @Nullable
    public ClassId getClassId() {
        final Long classId = getDocument().field(FIELD_CID, OType.LONG);
        if (classId == null) {
            return null;
        }
        return new ClassId(classId);
    }

    @Nullable
    public ClassHashId getClassHashId() {
        final byte[] data = getDocument().field(FIELD_HASH, OType.BINARY);
        if (data == null) {
            return null;
        }
        return new ClassHashId(data);
    }

    public byte[] getDef() {
        return null;
    }

    public static void assureClass(OSchema schema) {
        if (schema.existsClass(CLASS_NAME)) {
            return;
        }
        final OClass clazz = schema.createClass(CLASS_NAME);
        clazz.createProperty(FIELD_CID, OType.LONG);
        clazz.createProperty(FIELD_HASH, OType.BINARY);
        clazz.createProperty(FIELD_DEF, OType.BINARY);

        clazz.createIndex(INDEX_HASH, OClass.INDEX_TYPE.UNIQUE, FIELD_HASH);
        clazz.createIndex(INDEX_CID, OClass.INDEX_TYPE.UNIQUE, FIELD_CID);
        schema.save();
    }
}
