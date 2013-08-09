package com.dcrux.buran.refimpl.baseModules.fields.fieldPerformer;

import com.dcrux.buran.common.UserId;
import com.dcrux.buran.common.classDefinition.ClassDefinition;
import com.dcrux.buran.common.classDefinition.ClassFieldsDefinition;
import com.dcrux.buran.common.fields.getter.FieldGetInt;
import com.dcrux.buran.common.fields.getter.FieldGetPrim;
import com.dcrux.buran.common.fields.getter.IUnfieldedDataGetter;
import com.dcrux.buran.common.fields.setter.FieldRemove;
import com.dcrux.buran.common.fields.setter.FieldSetInt;
import com.dcrux.buran.common.fields.setter.IUnfieldedDataSetter;
import com.dcrux.buran.common.fields.types.IntegerType;
import com.dcrux.buran.refimpl.baseModules.BaseModule;
import com.dcrux.buran.refimpl.baseModules.fields.FieldConstraintViolationInt;
import com.dcrux.buran.refimpl.baseModules.fields.FieldPerformer;
import com.dcrux.buran.refimpl.baseModules.fields.ICommitInfo;
import com.dcrux.buran.refimpl.baseModules.nodeWrapper.CommonNode;
import com.dcrux.buran.refimpl.baseModules.nodeWrapper.FieldIndexAndClassId;
import com.dcrux.buran.refimpl.baseModules.nodeWrapper.LiveNode;
import com.orientechnologies.orient.core.metadata.schema.OType;

import java.io.Serializable;
import java.util.Set;

/**
 * Buran.
 *
 * @author: ${USER} Date: 03.07.13 Time: 19:15
 */
public class IntegerFieldPerformer extends FieldPerformer<IntegerType> {

    private final static Set<Class<? extends IUnfieldedDataGetter>> GETTERS =
            getters(FieldGetPrim.class, FieldGetInt.class);

    private final static Set<Class<? extends IUnfieldedDataSetter>> SETTERS = setters(FieldSetInt
            .class, FieldRemove.class);

    public static final IntegerFieldPerformer SINGLETON = new IntegerFieldPerformer();

    private static OType oTypeFrom(IntegerType integerType) {
        switch (integerType.getRequiredBits()) {
            case int8:
                return OType.BYTE;
            case int16:
                return OType.SHORT;
            case int32:
                return OType.INTEGER;
            case int64:
                return OType.LONG;
            default:
                throw new IllegalArgumentException("Unknown data type");
        }
    }

    @Override
    public boolean performSetter(BaseModule baseModule, UserId sender, CommonNode node,
            ClassDefinition classDefinition, IntegerType integerType,
            FieldIndexAndClassId fieldIndex, IUnfieldedDataSetter setter)
            throws FieldConstraintViolationInt {
        if (setter instanceof FieldSetInt) {
            final Number value = ((FieldSetInt) setter).getValue();
            if (!integerType.isValid(value)) {
                throw new FieldConstraintViolationInt(
                        "Invalid number. Check min-value / max-value.");
            }
            node.setFieldValue(fieldIndex, value, oTypeFrom(integerType));
            return true;
        }

        if (setter instanceof FieldRemove) {
            node.removeFieldValue(fieldIndex);
            return true;
        }
        throw new IllegalStateException("Unknown setter");
    }

    @Override
    public void validateAndCommit(BaseModule baseModule, UserId sender, CommonNode node,
            ClassDefinition classDefinition, IntegerType integerType,
            ClassFieldsDefinition.FieldEntry fieldEntry, FieldIndexAndClassId fieldIndex,
            ICommitInfo commitInfo) throws FieldConstraintViolationInt {
        if (fieldEntry.isRequired()) {
            Object value = node.getFieldValue(fieldIndex, oTypeFrom(integerType));
            if (value == null) {
                throw new FieldConstraintViolationInt("Field is missing.");
            }
        }
    }

    @Override
    public Serializable performGetter(BaseModule baseModule, LiveNode node,
            ClassDefinition classDefinition, IntegerType integerType,
            FieldIndexAndClassId fieldIndex, IUnfieldedDataGetter<?> dataGetter) {
        return (Serializable) node.getFieldValue(fieldIndex, oTypeFrom(integerType));
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
    public Class<IntegerType> supports() {
        return IntegerType.class;
    }
}
