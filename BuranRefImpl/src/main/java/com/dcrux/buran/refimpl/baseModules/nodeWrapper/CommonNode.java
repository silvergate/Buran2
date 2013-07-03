package com.dcrux.buran.refimpl.baseModules.nodeWrapper;

import com.dcrux.buran.common.UserId;
import com.dcrux.buran.common.classes.ClassId;
import com.dcrux.buran.common.fields.FieldIndex;
import com.dcrux.buran.refimpl.baseModules.common.DocumentWrapper;
import com.orientechnologies.orient.core.metadata.schema.OType;
import com.orientechnologies.orient.core.record.impl.ODocument;

/**
 * Buran.
 *
 * @author: ${USER} Date: 01.07.13 Time: 01:39
 */
public class CommonNode extends DocumentWrapper {

    /* Class prefixes */
    public static final String NODE_CLASS_PREFIX = "node";

    /* Common */
    public static final String FIELD_COMMON_LIVE = "live";
    public static final String INC_FIELD_SENDER = "sender";

    public CommonNode(ODocument document) {
        super(document);
    }

    public boolean isLive() {
        final Boolean live = getDocument().field(FIELD_COMMON_LIVE, OType.BOOLEAN);
        return (live == null) || (live);
    }

    public void setSender(UserId userId) {
        getDocument().field(INC_FIELD_SENDER, userId.getId(), OType.LONG);
    }

    public UserId getSender() {
        final long senderId = getDocument().field(INC_FIELD_SENDER, OType.LONG);
        return new UserId(senderId);
    }

    public ClassId getClassId() {
        return ClassNameUtils.getNodeClassId(getDocument().getClassName());
    }

    public static String fieldName(FieldIndex index) {
        return "f" + index.getIndex();
    }

    public void setFieldValue(FieldIndex index, Object value, OType type) {
        System.out.println("Setting value: " + value + ", at index: " + fieldName(index));
        getDocument().field(fieldName(index), value, type);
    }

    public Object getFieldValue(FieldIndex index, OType type) {
        return getDocument().field(fieldName(index), type);
    }


}
