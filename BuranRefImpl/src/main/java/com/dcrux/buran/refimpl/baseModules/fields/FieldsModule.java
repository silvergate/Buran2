package com.dcrux.buran.refimpl.baseModules.fields;

import com.dcrux.buran.common.UserId;
import com.dcrux.buran.common.fields.FieldIndex;
import com.dcrux.buran.common.fields.IDataGetter;
import com.dcrux.buran.common.fields.IDataSetter;
import com.dcrux.buran.common.fields.getter.*;
import com.dcrux.buran.common.fields.setter.DataSetter;
import com.dcrux.buran.common.fields.setter.IUnfieldedDataSetter;
import com.dcrux.buran.common.fields.setter.SetStr;
import com.dcrux.buran.refimpl.baseModules.BaseModule;
import com.dcrux.buran.refimpl.baseModules.common.Module;
import com.dcrux.buran.refimpl.baseModules.nodeWrapper.IncubationNode;
import com.dcrux.buran.refimpl.baseModules.nodeWrapper.LiveNode;
import com.orientechnologies.orient.core.metadata.schema.OType;

import java.util.Map;

/**
 * Buran.
 *
 * @author: ${USER} Date: 29.06.13 Time: 17:25
 */
public class FieldsModule extends Module<BaseModule> {

    public FieldsModule(BaseModule baseModule) {
        super(baseModule);
    }

    public void performUnfieldedSetter(UserId sender, IncubationNode incNode, FieldIndex fieldIndex,
            IUnfieldedDataSetter value) {
        if (value instanceof SetStr) {
            incNode.setFieldValue(fieldIndex, ((SetStr) value).getValue(), OType.STRING);
            return;
        }
    }

    public void performSetter(UserId sender, IncubationNode node, IDataSetter dataSetter) {
        if (dataSetter instanceof DataSetter) {
            final DataSetter batchSet = (DataSetter) dataSetter;
            for (final Map.Entry<FieldIndex, IUnfieldedDataSetter> entry : batchSet.getSetterMap()
                    .entrySet()) {
                performUnfieldedSetter(sender, node, entry.getKey(), entry.getValue());
            }
        } else {
            throw new IllegalArgumentException("Unkown Data Setter");
        }
    }

    private <T extends Object> T performUnfieldedGetter(LiveNode node, FieldIndex fieldIndex,
            IUnfieldedDataGetter<T> getter) {
        if (getter instanceof GetStr) {
            return (T) node.getFieldValue(fieldIndex, OType.STRING);
        }

        throw new IllegalArgumentException("Unknown getter");
    }

    public Object performGetter(LiveNode node, IDataGetter dataGetter) {
        if (dataGetter instanceof BatchGet) {
            final BatchGet batchGet = (BatchGet) dataGetter;
            final BatchGetResult result = new BatchGetResult();
            for (final Map.Entry<FieldIndex, IUnfieldedDataGetter<?>> entry : batchGet.getEntries()
                    .entrySet()) {
                final Object value = performUnfieldedGetter(node, entry.getKey(), entry.getValue());
                result.getValues().put(entry.getKey(), value);
            }
            return result;
        }

        if (dataGetter instanceof SingleGet) {
            final SingleGet singleGet = (SingleGet) dataGetter;
            final Object value = performUnfieldedGetter(node, singleGet.getFieldIndex(),
                    singleGet.getFieldGetter());
            return value;
        }

        throw new IllegalArgumentException("Unknown data setter");
    }
}
