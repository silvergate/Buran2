package com.dcrux.buran.refimpl.baseModules.fields.fieldPerformer;

import com.dcrux.buran.common.UserId;
import com.dcrux.buran.common.classDefinition.ClassDefinition;
import com.dcrux.buran.common.classDefinition.ClassFieldsDefinition;
import com.dcrux.buran.common.fields.FieldIndex;
import com.dcrux.buran.common.fields.getter.FieldGetBin;
import com.dcrux.buran.common.fields.getter.FieldGetBinLen;
import com.dcrux.buran.common.fields.getter.FieldGetPrim;
import com.dcrux.buran.common.fields.getter.IUnfieldedDataGetter;
import com.dcrux.buran.common.fields.setter.FieldAppendBin;
import com.dcrux.buran.common.fields.setter.FieldRemove;
import com.dcrux.buran.common.fields.setter.FieldSetBin;
import com.dcrux.buran.common.fields.setter.IUnfieldedDataSetter;
import com.dcrux.buran.common.fields.types.BinaryType;
import com.dcrux.buran.refimpl.baseModules.BaseModule;
import com.dcrux.buran.refimpl.baseModules.fields.FieldConstraintViolationInt;
import com.dcrux.buran.refimpl.baseModules.fields.FieldPerformer;
import com.dcrux.buran.refimpl.baseModules.fields.fieldPerformer.binary.BinaryUtil;
import com.dcrux.buran.refimpl.baseModules.nodeWrapper.CommonNode;
import com.dcrux.buran.refimpl.baseModules.nodeWrapper.LiveNode;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Set;

/**
 * Buran.
 *
 * @author: ${USER} Date: 03.07.13 Time: 19:15
 */
public class BinaryFieldPerformer extends FieldPerformer<BinaryType> {

    private final static Set<Class<? extends IUnfieldedDataGetter>> GETTERS =
            getters(FieldGetPrim.class, FieldGetBin.class, FieldGetBinLen.class);

    private final static Set<Class<? extends IUnfieldedDataSetter>> SETTERS = setters(FieldSetBin
            .class, FieldRemove.class, FieldAppendBin.class);

    public static final BinaryFieldPerformer SINGLETON = new BinaryFieldPerformer();

    @Override
    public boolean performSetter(BaseModule baseModule, UserId sender, CommonNode node,
            ClassDefinition classDefinition, BinaryType binaryType, FieldIndex fieldIndex,
            IUnfieldedDataSetter setter) throws FieldConstraintViolationInt {
        if (setter instanceof FieldSetBin) {
            final byte[] value = ((FieldSetBin) setter).getValue();
            BinaryUtil.createOfNonExistent(node, fieldIndex);
            BinaryUtil.empty(baseModule, node, fieldIndex);
            final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(value);
            try {
                BinaryUtil.append(baseModule, node, fieldIndex, byteArrayInputStream);
            } catch (IOException e) {
                throw new IllegalStateException("IOException", e);
            }
            return true;
        }

        if (setter instanceof FieldAppendBin) {
            final byte[] value = ((FieldAppendBin) setter).getValue();
            BinaryUtil.createOfNonExistent(node, fieldIndex);
            final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(value);
            try {
                BinaryUtil.append(baseModule, node, fieldIndex, byteArrayInputStream);
            } catch (IOException e) {
                throw new IllegalStateException("IOException", e);
            }
            return true;
        }

        if (setter instanceof FieldRemove) {
            if (BinaryUtil.exists(node, fieldIndex)) {
                BinaryUtil.remove(baseModule, node, fieldIndex);
            }
            return true;
        }
        throw new IllegalStateException("Unknown setter");
    }

    @Override
    public void performCheck(UserId sender, CommonNode node, ClassDefinition classDefinition,
            BinaryType binaryType, ClassFieldsDefinition.FieldEntry fieldEntry,
            FieldIndex fieldIndex) throws FieldConstraintViolationInt {
        if (fieldEntry.isRequired()) {
            boolean exists = BinaryUtil.exists(node, fieldIndex);
            if (!exists) {
                throw new FieldConstraintViolationInt("Field is missing.");
            }
        }
    }

    @Override
    public Serializable performGetter(BaseModule baseModule, LiveNode node,
            ClassDefinition classDefinition, BinaryType binaryType, FieldIndex fieldIndex,
            IUnfieldedDataGetter<?> dataGetter) {
        if ((dataGetter instanceof FieldGetPrim) || (dataGetter instanceof FieldGetBinLen)) {
            final Long len = BinaryUtil.getSize(node, fieldIndex);
            return len;
        } else if (dataGetter instanceof FieldGetBin) {
            final FieldGetBin fieldGetBin = (FieldGetBin) dataGetter;
            final ByteArrayOutputStream bos = new ByteArrayOutputStream();
            try {
                BinaryUtil.read(baseModule, node, fieldIndex, fieldGetBin.getSkip(),
                        fieldGetBin.getNumber(), bos);
                return bos.toByteArray();
            } catch (IOException e) {
                throw new IllegalArgumentException("IOException: ", e);
            } finally {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings
                    // | File Templates.
                }
            }
        } else {
            throw new IllegalArgumentException("TODO: Implement me");
        }
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
    public Class<BinaryType> supports() {
        return BinaryType.class;
    }
}
