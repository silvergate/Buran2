package com.dcrux.buran.refimpl.modules.domains;

import com.dcrux.buran.common.domain.DomainHashId;
import com.dcrux.buran.common.domain.DomainId;
import com.dcrux.buran.refimpl.modules.BaseModule;
import com.dcrux.buran.refimpl.modules.common.DocumentWrapper;
import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.metadata.schema.OSchema;
import com.orientechnologies.orient.core.metadata.schema.OType;
import com.orientechnologies.orient.core.record.impl.ODocument;

/**
 * Buran.
 *
 * @author: ${USER} Date: 13.08.13 Time: 22:25
 */
public class DomainWrapper extends DocumentWrapper {
    public static final String ORIENT_CLASS = "domains";

    public static final String FIELD_ID = "id";
    public static final String FIELD_HASH = "hash";

    public static final String IDX_ID = "idxId";
    public static final String IDX_HASH = "idxHash";

    public DomainWrapper(ODocument document) {
        super(document);
    }

    public static DomainWrapper create(DomainHashId domainHashId, DomainId domainId) {
        ODocument doc = new ODocument(ORIENT_CLASS);
        doc.field(FIELD_HASH, domainHashId.getId(), OType.STRING);
        doc.field(FIELD_ID, domainId.getId(), OType.LONG);
        return new DomainWrapper(doc);
    }

    public static DomainWrapper create(DomainId domainId) {
        return create(new DomainHashId(""), domainId);
    }

    public static void setupDb(BaseModule baseModule) {
        final OSchema schema = baseModule.getDb().getMetadata().getSchema();
        if (!schema.existsClass(ORIENT_CLASS)) {
            final OClass clazz = schema.createClass(ORIENT_CLASS);
            clazz.createProperty(FIELD_ID, OType.LONG);
            clazz.createProperty(FIELD_HASH, OType.STRING);

            /* Source NID and Field Index */
            clazz.createIndex(IDX_ID, OClass.INDEX_TYPE.UNIQUE, FIELD_ID);

            /* Target NID, src classId, src fieldIndex */
            clazz.createIndex(IDX_HASH, OClass.INDEX_TYPE.UNIQUE, FIELD_HASH);

            schema.save();
        }
    }

    public DomainHashId getDomainHashId() {
        final String str = getDocument().field(FIELD_HASH, OType.STRING);
        return new DomainHashId(str);
    }

    public DomainId getDomainId() {
        final Long id = getDocument().field(FIELD_ID, OType.LONG);
        return new DomainId(id);
    }
}
