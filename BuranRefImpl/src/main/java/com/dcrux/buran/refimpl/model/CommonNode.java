package com.dcrux.buran.refimpl.model;

import com.dcrux.buran.ClassId;
import com.dcrux.buran.UserId;
import com.dcrux.buran.fields.FieldIndex;
import com.dcrux.buran.refimpl.baseModules.fields.DocFields;
import com.dcrux.buran.refimpl.common.DocumentWrapper;
import com.dcrux.buran.refimpl.dao.ClassNameUtils;
import com.orientechnologies.orient.core.metadata.schema.OType;
import com.orientechnologies.orient.core.record.impl.ODocument;

/**
 * Buran.
 *
 * @author: ${USER} Date: 01.07.13 Time: 01:39
 */
public class CommonNode extends DocumentWrapper {

    public CommonNode(ODocument document) {
        super(document);
    }

    public boolean isLive() {
        final Boolean live = getDocument().field(DocFields.FIELD_COMMON_LIVE, OType.BOOLEAN);
        return (live == null) || (live);
    }

    public void setSender(UserId userId) {
        getDocument().field(DocFields.INC_FIELD_SENDER, userId.getId(), OType.LONG);
    }

    public UserId getSender() {
        final long senderId = getDocument().field(DocFields.INC_FIELD_SENDER, OType.LONG);
        return new UserId(senderId);
    }

    public ClassId getClassId() {
        return ClassNameUtils.getNodeClassId(getDocument().getClassName());
    }

    public void setFieldValue(FieldIndex index, Object value, OType type) {
        System.out.println("Setting value: " + value + ", at index: " + DocFields.fieldName(index));
        getDocument().field(DocFields.fieldName(index), value, type);
    }

    public Object getFieldValue(FieldIndex index, OType type) {
        return getDocument().field(DocFields.fieldName(index), type);
    }


}
