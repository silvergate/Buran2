package com.dcrux.buran.refimpl.baseModules.fields;

import com.dcrux.buran.common.UserId;
import com.dcrux.buran.common.classDefinition.ClassDefinition;
import com.dcrux.buran.common.classDefinition.ClassFieldsDefinition;
import com.dcrux.buran.common.exceptions.NodeClassNotFoundException;
import com.dcrux.buran.common.fields.FieldIndex;
import com.dcrux.buran.common.fields.IFieldGetter;
import com.dcrux.buran.common.fields.IFieldSetter;
import com.dcrux.buran.common.fields.getter.*;
import com.dcrux.buran.common.fields.setter.FieldRemove;
import com.dcrux.buran.common.fields.setter.FieldRemoveAll;
import com.dcrux.buran.common.fields.setter.FieldSetter;
import com.dcrux.buran.common.fields.setter.IUnfieldedDataSetter;
import com.dcrux.buran.common.fields.typeDef.ITypeDef;
import com.dcrux.buran.refimpl.baseModules.BaseModule;
import com.dcrux.buran.refimpl.baseModules.common.Module;
import com.dcrux.buran.refimpl.baseModules.nodeWrapper.CommonNode;
import com.dcrux.buran.refimpl.baseModules.nodeWrapper.LiveNode;

import java.io.Serializable;
import java.util.Map;

/**
 * Buran.
 *
 * @author: ${USER} Date: 29.06.13 Time: 17:25
 */
public class FieldsModule extends Module<BaseModule> {

    private static final FieldPerformerRegistry REGISTRY;

    static {
        REGISTRY = new FieldPerformerRegistry();
        FieldPerformerRegistryUtil.register(REGISTRY);
    }

    public FieldsModule(BaseModule baseModule) {
        super(baseModule);
    }

    public boolean performSetter(UserId sender, CommonNode node, IFieldSetter dataSetter)
            throws NodeClassNotFoundException, FieldConstraintViolationInt {
        if (dataSetter instanceof FieldRemoveAll) {
            final ClassDefinition classDef =
                    getBase().getClassesModule().getClassDefById(node.getClassId());
            final FieldSetter fs = new FieldSetter();
            for (final FieldIndex index : classDef.getFields().getFieldIndexes()) {
                fs.add(index, FieldRemove.c());
            }
            return performSetter(sender, node, fs);
        }

        if (dataSetter instanceof FieldSetter) {
            final ClassDefinition classDef =
                    getBase().getClassesModule().getClassDefById(node.getClassId());
            final ClassFieldsDefinition classDefFields = classDef.getFields();
            final FieldSetter batchSet = (FieldSetter) dataSetter;
            boolean nodeChanged = false;
            for (final Map.Entry<FieldIndex, IUnfieldedDataSetter> entry : batchSet.getSetterMap()
                    .entrySet()) {
                final ClassFieldsDefinition.FieldEntry fieldEntry =
                        classDefFields.getFieldEntries().get(entry.getKey());
                if (fieldEntry == null) {
                    throw new IllegalArgumentException("No such field is declared in class");
                }
                final ITypeDef typeDef = fieldEntry.getTypeDef();
                final IFieldPerformer performer = REGISTRY.get(typeDef.getClass());
                if (performer == null) {
                    throw new IllegalArgumentException("No performer for this field type found");
                }
                if (!performer.supportedSetters().contains(entry.getValue().getClass())) {
                    throw new IllegalArgumentException(
                            "Performer does not support setter: " + entry.getValue().getClass());
                }

                final boolean unc = performer
                        .performSetter(sender, node, classDef, typeDef, entry.getKey(),
                                entry.getValue());

                if (unc) {
                    nodeChanged = true;
                }
            }
            return nodeChanged;
        }

        throw new IllegalArgumentException("Unkown Data Setter");
    }

    private <T extends Serializable> T performUnfieldedGetter(LiveNode node,
            ClassDefinition classDefinition, FieldIndex fieldIndex,
            IUnfieldedDataGetter<T> getter) {
        final ClassFieldsDefinition.FieldEntry fieldEntry =
                classDefinition.getFields().getFieldEntries().get(fieldIndex);
        final ITypeDef typeDef = fieldEntry.getTypeDef();
        final IFieldPerformer performer = REGISTRY.get(typeDef.getClass());
        if (performer == null) {
            throw new IllegalArgumentException("No performer for this field type found");
        }
        if (!performer.supportedGetters().contains(getter.getClass())) {
            throw new IllegalArgumentException(
                    "Performer does not support getter: " + getter.getClass());
        }

        return (T) performer.performGetter(node, classDefinition, typeDef, fieldIndex, getter);
    }

    public Serializable performGetter(LiveNode node, IFieldGetter dataGetter)
            throws NodeClassNotFoundException {
        final ClassDefinition classDef =
                getBase().getClassesModule().getClassDefById(node.getClassId());

        if (dataGetter instanceof FieldGet) {
            final FieldGet fieldGet = (FieldGet) dataGetter;
            final FieldGetResult result = new FieldGetResult();
            for (final Map.Entry<FieldIndex, IUnfieldedDataGetter<?>> entry : fieldGet.getEntries()
                    .entrySet()) {
                final Serializable value =
                        performUnfieldedGetter(node, classDef, entry.getKey(), entry.getValue());
                result.getValues().put(entry.getKey(), value);
            }
            return result;
        }

        if (dataGetter instanceof FieldGetAll) {
            final FieldGetPrim fieldGet = FieldGetPrim.SINGLETON;
            final FieldGetResult result = new FieldGetResult();
            for (final Map.Entry<FieldIndex, ClassFieldsDefinition.FieldEntry> entry : classDef
                    .getFields().getFieldEntries().entrySet()) {
                final Serializable value =
                        performUnfieldedGetter(node, classDef, entry.getKey(), fieldGet);
                result.getValues().put(entry.getKey(), value);
            }
            return result;
        }

        if (dataGetter instanceof SingleGet) {
            final SingleGet singleGet = (SingleGet) dataGetter;
            final Serializable value =
                    performUnfieldedGetter(node, classDef, singleGet.getFieldIndex(),
                            singleGet.getFieldGetter());
            return value;
        }

        throw new IllegalArgumentException("Unknown data setter");
    }
}
