package com.dcrux.buran.refimpl.baseModules.nodeWrapper;

import com.dcrux.buran.common.UserId;
import com.dcrux.buran.common.classes.ClassId;
import com.dcrux.buran.common.fields.FieldIndex;
import com.dcrux.buran.refimpl.baseModules.common.DocumentWrapper;
import com.dcrux.buran.refimpl.baseModules.common.OIncNid;
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
    public static final String FIELD_MARKED_FOR_DELETION = "markDel";

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

    public static String fieldName(FieldIndex index, String append) {
        return "f" + append + index.getIndex();
    }

    public void setFieldValue(FieldIndex index, Object value, OType type) {
        /*System.out.println("Setting value: " + value + ", at indexAndNotify: " + fieldName
                (index));*/
        getDocument().field(fieldName(index), value, type);
    }

    public void removeFieldValue(FieldIndex index) {
        getDocument().removeField(fieldName(index));
    }

    public Object getFieldValue(FieldIndex index, OType type) {
        return getDocument().field(fieldName(index), type);
    }

    public Object getFieldValue(FieldIndex index) {
        return getDocument().field(fieldName(index));
    }

    public boolean hasField(FieldIndex index) {
        return getDocument().containsField(fieldName(index));
    }

    public void setFieldValue(FieldIndex index, String append, Object value, OType type) {
        /*System.out.println("Setting value: " + value + ", at indexAndNotify: " + fieldName
                (index));*/
        getDocument().field(fieldName(index, append), value, type);
    }


    public void removeFieldValue(FieldIndex index, String append) {
        getDocument().removeField(fieldName(index, append));
    }

    public Object getFieldValue(FieldIndex index, String append, OType type) {
        return getDocument().field(fieldName(index, append), type);
    }

    public Object getFieldValue(FieldIndex index, String append) {
        return getDocument().field(fieldName(index, append));
    }

    public boolean hasField(FieldIndex index, String append) {
        return getDocument().containsField(fieldName(index, append));
    }

    public void markForDeletion() {
        getDocument().field(FIELD_MARKED_FOR_DELETION, true, OType.BOOLEAN);
    }

    public boolean isMarkedForDeletion() {
        final Boolean marked = getDocument().field(FIELD_MARKED_FOR_DELETION, OType.BOOLEAN);
        return (marked != null) && marked;
    }

    public void deleteNow() {
        getDocument().clear();
        markForDeletion();
    }

    public OIncNid getIncNid() {
        return new OIncNid(getDocument().getIdentity());
    }
}
