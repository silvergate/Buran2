package com.dcrux.buran.refimpl.baseModules.fields;

import com.dcrux.buran.common.UserId;
import com.dcrux.buran.common.classDefinition.ClassDefinition;
import com.dcrux.buran.common.classDefinition.ClassFieldsDefinition;
import com.dcrux.buran.common.classes.ClassId;
import com.dcrux.buran.common.exceptions.NodeClassNotFoundException;
import com.dcrux.buran.common.fields.*;
import com.dcrux.buran.common.fields.getter.*;
import com.dcrux.buran.common.fields.setter.FieldRemove;
import com.dcrux.buran.common.fields.setter.FieldRemoveAll;
import com.dcrux.buran.common.fields.setter.FieldSetter;
import com.dcrux.buran.common.fields.setter.IUnfieldedDataSetter;
import com.dcrux.buran.common.fields.typeDef.ITypeDef;
import com.dcrux.buran.refimpl.baseModules.BaseModule;
import com.dcrux.buran.refimpl.baseModules.classes.ClassDefCache;
import com.dcrux.buran.refimpl.baseModules.common.Module;
import com.dcrux.buran.refimpl.baseModules.nodeWrapper.CommonNode;
import com.dcrux.buran.refimpl.baseModules.nodeWrapper.LiveNode;

import java.io.Serializable;
import java.text.MessageFormat;
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

    public FieldPerformerRegistry getFieldPerformerRegistry() {
        return REGISTRY;
    }

    public boolean performSetter(UserId sender, CommonNode node, IFieldSetter dataSetter)
            throws NodeClassNotFoundException, FieldConstraintViolationInt {
        ClassDefCache classDefCache = new ClassDefCache();
        return performSetter(sender, node, dataSetter, classDefCache);
    }

    private boolean performSetter(UserId sender, CommonNode node, IFieldSetter dataSetter,
            ClassDefCache cache) throws NodeClassNotFoundException, FieldConstraintViolationInt {
        if (dataSetter instanceof FieldRemoveAll) {
            final FieldRemoveAll fieldRemoveAll = (FieldRemoveAll) dataSetter;
            final ClassDefinition classDef =
                    cache.getClassDef(getBase(), fieldRemoveAll.getClassId());
            final FieldSetter fs = new FieldSetter();
            for (final FieldIndex index : classDef.getFields().getFieldIndexes()) {
                fs.add(new FieldTarget(fieldRemoveAll.getClassId(), index), FieldRemove.c());
            }
            return performSetter(sender, node, fs);
        }

        if (dataSetter instanceof FieldSetter) {
            /*final ClassDefinition classDef =
                    getBase().getClassesModule().getClassDefById(node.getPrimaryClassId());*/
            //final ClassFieldsDefinition classDefFields = classDef.getFields();
            final FieldSetter batchSet = (FieldSetter) dataSetter;
            boolean nodeChanged = false;
            for (final Map.Entry<IFieldTarget, IUnfieldedDataSetter> entry : batchSet.getSetterMap()
                    .entrySet()) {
                final IFieldTarget fieldTarget = entry.getKey();
                if (fieldTarget.is(IFieldTarget.TYPE_FIELD_TARGET)) {
                    final FieldTarget fieldTargetImpl =
                            fieldTarget.get(IFieldTarget.TYPE_FIELD_TARGET);
                    final FieldIndex fieldIndex = fieldTargetImpl.getFieldIndex();
                    final ClassId classId = fieldTargetImpl.getClassId();
                    if (!node.isTypeOf(classId)) {
                        throw new IllegalArgumentException("Node is not of right class");
                    }
                    final ClassDefinition classDef = cache.getClassDef(getBase(), classId);
                    final ClassFieldsDefinition.FieldEntry fieldEntry =
                            classDef.getFields().getFieldEntries().get(fieldIndex);
                    if (fieldEntry == null) {
                        throw new IllegalArgumentException("No such field is declared in class");
                    }

                    final ITypeDef typeDef = fieldEntry.getTypeDef();
                    final IFieldPerformer performer = REGISTRY.get(typeDef.getClass());
                    if (performer == null) {
                        throw new IllegalArgumentException(
                                "No performer for this field type found");
                    }
                    if (!performer.supportedSetters().contains(entry.getValue().getClass())) {
                        throw new IllegalArgumentException("Performer does not support setter: " +
                                entry.getValue().getClass());
                    }

                    final boolean unc = performer
                            .performSetter(getBase(), sender, node, classDef, typeDef, fieldIndex,
                                    entry.getValue());

                    if (unc) {
                        nodeChanged = true;
                    }
                } else {
                    throw new UnsupportedOperationException("Not yet implemnted");
                }
            }
            return nodeChanged;
        }

        throw new IllegalArgumentException("Unkown Data Setter");
    }

    public <T extends Serializable> T performUnfieldedGetter(LiveNode node, FieldTarget fieldTarget,
            ClassDefCache cache, IUnfieldedDataGetter<T> getter) throws NodeClassNotFoundException {
        FieldIndex fieldIndex = fieldTarget.getFieldIndex();
        ClassId classId = fieldTarget.getClassId();
        ClassDefinition classDefinition = cache.getClassDef(getBase(), classId);

        if (!node.isTypeOf(classId)) {
            /* If wrong class, return null */
            return null;
        }

        final ClassFieldsDefinition.FieldEntry fieldEntry =
                classDefinition.getFields().getFieldEntries().get(fieldIndex);
        final ITypeDef typeDef = fieldEntry.getTypeDef();
        final IFieldPerformer performer = REGISTRY.get(typeDef.getClass());
        if (performer == null) {
            throw new IllegalArgumentException(MessageFormat.format("No performer for this field " +
                    "type " +
                    "found. Type: {0}", typeDef.getClass()));
        }
        if (!performer.supportedGetters().contains(getter.getClass())) {
            throw new IllegalArgumentException(
                    "Performer does not support getter: " + getter.getClass());
        }

        return (T) performer
                .performGetter(getBase(), node, classDefinition, typeDef, fieldIndex, getter);
    }

    public Serializable performGetter(LiveNode node, IFieldGetter dataGetter)
            throws NodeClassNotFoundException {
        ClassDefCache classDefCache = new ClassDefCache();
        return performGetter(node, dataGetter, classDefCache);
    }

    private Serializable performGetter(LiveNode node, IFieldGetter dataGetter, ClassDefCache cache)
            throws NodeClassNotFoundException {
        /*final ClassDefinition classDef =
                getBase().getClassesModule().getClassDefById(node.getPrimaryClassId());*/

        if (dataGetter instanceof FieldGet) {
            final FieldGet fieldGet = (FieldGet) dataGetter;
            final FieldGetResult result = new FieldGetResult();
            for (final Map.Entry<IFieldTarget, IUnfieldedDataGetter<?>> entry : fieldGet
                    .getEntries().entrySet()) {
                final SingleGet singleGet = new SingleGet(entry.getKey(), entry.getValue());
                final Serializable value = performGetter(node, singleGet, cache);
                result.getValues().put(entry.getKey(), value);
            }
            return result;
        }

        if (dataGetter instanceof FieldGetAll) {
            /*final FieldGetPrim fieldGet = FieldGetPrim.SINGLETON;
            final FieldGetResult result = new FieldGetResult();
            for (final Map.Entry<FieldIndex, ClassFieldsDefinition.FieldEntry> entry : classDef
                    .getFields().getFieldEntries().entrySet()) {
                final Serializable value =
                        performUnfieldedGetter(node, classDef, entry.getKey(), fieldGet);
                result.getValues().put(entry.getKey(), value);
            }
            return result;*/
            throw new UnsupportedOperationException("Not yet implemented");
        }

        if (dataGetter instanceof SingleGet) {
            final SingleGet singleGet = (SingleGet) dataGetter;

            final IFieldTarget fieldTarget = singleGet.getFieldIndex();
            if (fieldTarget.is(IFieldTarget.TYPE_FIELD_TARGET)) {
                final FieldTarget fieldTargetImpl = fieldTarget.get(IFieldTarget.TYPE_FIELD_TARGET);
                final Serializable value = performUnfieldedGetter(node, fieldTargetImpl, cache,
                        singleGet.getFieldGetter());
                return value;
            } else if (fieldTarget.is(IFieldTarget.TYPE_NODE_FIELD)) {
                throw new UnsupportedOperationException("Not yet implemented");
            } else {
                throw new IllegalArgumentException("Unknown type");
            }
        }

        throw new IllegalArgumentException("Unknown data setter");
    }
}
