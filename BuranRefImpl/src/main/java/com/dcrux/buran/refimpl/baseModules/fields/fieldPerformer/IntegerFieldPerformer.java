package com.dcrux.buran.refimpl.baseModules.fields.fieldPerformer;

import com.dcrux.buran.common.UserId;
import com.dcrux.buran.common.classDefinition.ClassDefinition;
import com.dcrux.buran.common.classDefinition.ClassFieldsDefinition;
import com.dcrux.buran.common.fields.FieldIndex;
import com.dcrux.buran.common.fields.getter.GetInt;
import com.dcrux.buran.common.fields.getter.GetPrim;
import com.dcrux.buran.common.fields.getter.IUnfieldedDataGetter;
import com.dcrux.buran.common.fields.setter.IUnfieldedDataSetter;
import com.dcrux.buran.common.fields.setter.SetInt;
import com.dcrux.buran.common.fields.types.IntegerType;
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
public class IntegerFieldPerformer extends FieldPerformer<IntegerType> {

    private final static Set<Class<? extends IUnfieldedDataGetter<?>>> GETTERS =
            getters(GetInt.class, GetPrim.class);

    private final static Set<Class<? extends IUnfieldedDataSetter>> SETTERS = setters(SetInt.class);

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
    public boolean performSetter(UserId sender, CommonNode node, ClassDefinition classDefinition,
            IntegerType integerType, FieldIndex fieldIndex, IUnfieldedDataSetter setter)
            throws FieldConstraintViolationInt {
        final Number value = ((SetInt) setter).getValue();
        if (!integerType.isValid(value)) {
            throw new FieldConstraintViolationInt("Invalid number. Check min-value / max-value.");
        }

        node.setFieldValue(fieldIndex, value, oTypeFrom(integerType));
        return true;
    }

    @Override
    public void performCheck(UserId sender, CommonNode node, ClassDefinition classDefinition,
            IntegerType integerType, ClassFieldsDefinition.FieldEntry fieldEntry,
            FieldIndex fieldIndex) throws FieldConstraintViolationInt {
        if (fieldEntry.isRequired()) {
            Object value = node.getFieldValue(fieldIndex, oTypeFrom(integerType));
            if (value == null) {
                throw new FieldConstraintViolationInt("Field is missing.");
            }
        }
    }

    @Override
    public Serializable performGetter(LiveNode node, ClassDefinition classDefinition,
            IntegerType integerType, FieldIndex fieldIndex, IUnfieldedDataGetter<?> dataGetter) {
        return (Serializable) node.getFieldValue(fieldIndex, oTypeFrom(integerType));
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
    public Class<IntegerType> supports() {
        return IntegerType.class;
    }
}
