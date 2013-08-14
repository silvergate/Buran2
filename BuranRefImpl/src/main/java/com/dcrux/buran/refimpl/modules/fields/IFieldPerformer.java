package com.dcrux.buran.refimpl.modules.fields;

import com.dcrux.buran.common.UserId;
import com.dcrux.buran.common.classDefinition.ClassDefinition;
import com.dcrux.buran.common.classDefinition.ClassFieldsDefinition;
import com.dcrux.buran.common.fields.getter.IUnfieldedDataGetter;
import com.dcrux.buran.common.fields.setter.IUnfieldedDataSetter;
import com.dcrux.buran.common.fields.typeDef.ITypeDef;
import com.dcrux.buran.refimpl.modules.BaseModule;
import com.dcrux.buran.refimpl.modules.nodeWrapper.CommonNode;
import com.dcrux.buran.refimpl.modules.nodeWrapper.FieldIndexAndClassId;
import com.dcrux.buran.refimpl.modules.nodeWrapper.LiveNode;

import java.io.Serializable;
import java.util.Set;

/**
 * Buran.
 *
 * @author: ${USER} Date: 03.07.13 Time: 18:57
 */
public interface IFieldPerformer<TFieldType extends ITypeDef> {

    boolean performSetter(BaseModule baseModule, UserId sender, CommonNode node,
            ClassDefinition classDefinition, TFieldType fieldType, FieldIndexAndClassId fieldIndex,
            IUnfieldedDataSetter setter) throws FieldConstraintViolationInt;

    void validateAndCommit(BaseModule baseModule, UserId sender, CommonNode node,
            ClassDefinition classDefinition, TFieldType fieldType,
            ClassFieldsDefinition.FieldEntry fieldEntry, FieldIndexAndClassId fieldIndex,
            ICommitInfo commitInfo) throws FieldConstraintViolationInt;

    Serializable performGetter(BaseModule baseModule, LiveNode node,
            ClassDefinition classDefinition, TFieldType fieldType, FieldIndexAndClassId fieldIndex,
            IUnfieldedDataGetter<?> dataGetter);

    Set<Class<? extends IUnfieldedDataGetter>> supportedGetters();

    Set<Class<? extends IUnfieldedDataSetter>> supportedSetters();

    Class<TFieldType> supports();
}
