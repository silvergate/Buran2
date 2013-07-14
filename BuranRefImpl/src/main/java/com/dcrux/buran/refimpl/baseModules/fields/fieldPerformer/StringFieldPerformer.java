package com.dcrux.buran.refimpl.baseModules.fields.fieldPerformer;

import com.dcrux.buran.common.UserId;
import com.dcrux.buran.common.classDefinition.ClassDefinition;
import com.dcrux.buran.common.classDefinition.ClassFieldsDefinition;
import com.dcrux.buran.common.fields.FieldIndex;
import com.dcrux.buran.common.fields.getter.FieldGetPrim;
import com.dcrux.buran.common.fields.getter.FieldGetStr;
import com.dcrux.buran.common.fields.getter.IUnfieldedDataGetter;
import com.dcrux.buran.common.fields.setter.FieldRemove;
import com.dcrux.buran.common.fields.setter.FieldSetStr;
import com.dcrux.buran.common.fields.setter.IUnfieldedDataSetter;
import com.dcrux.buran.common.fields.types.StringType;
import com.dcrux.buran.refimpl.baseModules.BaseModule;
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

    private final static Set<Class<? extends IUnfieldedDataGetter>> GETTERS =
            getters(FieldGetPrim.class, FieldGetStr.class);

    private final static Set<Class<? extends IUnfieldedDataSetter>> SETTERS = setters(FieldSetStr
            .class, FieldRemove.class);

    public static final StringFieldPerformer SINGLETON = new StringFieldPerformer();

    @Override
    public boolean performSetter(BaseModule baseModule, UserId sender, CommonNode node,
            ClassDefinition classDefinition, StringType stringType, FieldIndex fieldIndex,
            IUnfieldedDataSetter setter) throws FieldConstraintViolationInt {
        if (setter instanceof FieldSetStr) {
            final String value = ((FieldSetStr) setter).getValue();
            if (!stringType.isValid(value)) {
                throw new FieldConstraintViolationInt("Invalid string. Check length.");
            }
            node.setFieldValue(fieldIndex, value, OType.STRING);
            return true;
        }

        if (setter instanceof FieldRemove) {
            node.removeFieldValue(fieldIndex);
            return true;
        }
        throw new IllegalStateException("Unknown setter");
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
    public Serializable performGetter(BaseModule baseModule, LiveNode node,
            ClassDefinition classDefinition, StringType stringType, FieldIndex fieldIndex,
            IUnfieldedDataGetter<?> dataGetter) {
        if (dataGetter instanceof FieldGetPrim) {
            return (Serializable) node.getFieldValue(fieldIndex, OType.STRING);
        }
        throw new IllegalArgumentException("Unknown getter");
    }

    @Override
    public Set<Class<? extends IUnfieldedDataGetter>> supportedGetters() {
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
