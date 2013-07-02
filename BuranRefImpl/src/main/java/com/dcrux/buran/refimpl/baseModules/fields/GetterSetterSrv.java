package com.dcrux.buran.refimpl.baseModules.fields;

import com.dcrux.buran.UserId;
import com.dcrux.buran.fields.FieldIndex;
import com.dcrux.buran.fields.getter.GetStr;
import com.dcrux.buran.fields.getter.IDataGetter;
import com.dcrux.buran.fields.setter.IDataSetter;
import com.dcrux.buran.fields.setter.SetterString;
import com.dcrux.buran.refimpl.baseModules.BaseModule;
import com.dcrux.buran.refimpl.model.IncubationNode;
import com.dcrux.buran.refimpl.model.LiveNode;
import com.orientechnologies.orient.core.metadata.schema.OType;

/**
 * Buran.
 *
 * @author: ${USER} Date: 29.06.13 Time: 17:25
 */
public class GetterSetterSrv {

    private final BaseModule impl;

    public GetterSetterSrv(BaseModule impl) {
        this.impl = impl;
    }

    public void performSetter(UserId sender, IncubationNode incNode, FieldIndex fieldIndex,
            IDataSetter value) {
        if (value instanceof SetterString) {
            incNode.setFieldValue(fieldIndex, ((SetterString) value).getValue(), OType.STRING);
            return;
        }
    }

    public <T extends Object> T performGetter(LiveNode node, FieldIndex fieldIndex,
            IDataGetter<T> getter) {
        if (getter instanceof GetStr) {
            return (T) node.getFieldValue(fieldIndex, OType.STRING);
        }

        throw new IllegalArgumentException("Unknown getter");
    }
}
