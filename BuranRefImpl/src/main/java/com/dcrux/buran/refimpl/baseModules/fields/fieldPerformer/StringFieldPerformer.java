package com.dcrux.buran.refimpl.baseModules.fields.fieldPerformer;

import com.dcrux.buran.common.UserId;
import com.dcrux.buran.common.classDefinition.ClassDefinition;
import com.dcrux.buran.common.classDefinition.ClassFieldsDefinition;
import com.dcrux.buran.common.fields.FieldIndex;
import com.dcrux.buran.common.fields.getter.GetPrim;
import com.dcrux.buran.common.fields.getter.GetStr;
import com.dcrux.buran.common.fields.getter.IUnfieldedDataGetter;
import com.dcrux.buran.common.fields.setter.IUnfieldedDataSetter;
import com.dcrux.buran.common.fields.setter.SetStr;
import com.dcrux.buran.common.fields.types.StringType;
import com.dcrux.buran.refimpl.baseModules.fields.FieldConstraintViolationInt;
import com.dcrux.buran.refimpl.baseModules.fields.FieldPerformer;
import com.dcrux.buran.refimpl.baseModules.nodeWrapper.CommonNode;
import com.dcrux.buran.refimpl.baseModules.nodeWrapper.LiveNode;
import com.orientechnologies.orient.core.metadata.schema.OType;

import java.io.Serializable;
import java.util.Set;

/**
 * Buran.
 *
 * @author: ${USER} Date: 03.07.13 Time: 19:15
 */
public class StringFieldPerformer extends FieldPerformer<StringType> {

    private final static Set<Class<? extends IUnfieldedDataGetter<?>>> GETTERS =
            getters(GetStr.class, GetPrim.class);

    private final static Set<Class<? extends IUnfieldedDataSetter>> SETTERS = setters(SetStr.class);

    public static final StringFieldPerformer SINGLETON = new StringFieldPerformer();

    @Override
    public boolean performSetter(UserId sender, CommonNode node, ClassDefinition classDefinition,
            StringType stringType, FieldIndex fieldIndex, IUnfieldedDataSetter setter)
            throws FieldConstraintViolationInt {
        final String value = ((SetStr) setter).getValue();
        if (!stringType.isValid(value)) {
            throw new FieldConstraintViolationInt("Invalid string. Check length.");
        }

        node.setFieldValue(fieldIndex, value, OType.STRING);
        return true;
    }

    @Override
    public void performCheck(UserId sender, CommonNode node, ClassDefinition classDefinition,
            StringType stringType, ClassFieldsDefinition.FieldEntry fieldEntry,
            FieldIndex fieldIndex) throws FieldConstraintViolationInt {
        if (fieldEntry.isRequired()) {
            Object value = node.getFieldValue(fieldIndex, OType.STRING);
            if (value == null) {
                throw new FieldConstraintViolationInt("Field is missing.");
            }
        }
    }

    @Override
    public Serializable performGetter(LiveNode node, ClassDefinition classDefinition,
            StringType stringType, FieldIndex fieldIndex, IUnfieldedDataGetter<?> dataGetter) {
        return (Serializable) node.getFieldValue(fieldIndex, OType.STRING);
    }

    @Override
    public Set<Class<? extends IUnfieldedDataGetter<?>>> supportedGetters() {
        return GETTERS;
    }

    @Override
    public Set<Class<? extends IUnfieldedDataSetter>> supportedSetters() {
        return SETTERS;
    }

    @Override
    public Class<StringType> supports() {
        return StringType.class;
    }
}
