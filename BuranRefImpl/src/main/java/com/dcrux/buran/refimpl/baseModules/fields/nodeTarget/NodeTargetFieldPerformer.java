package com.dcrux.buran.refimpl.baseModules.fields.nodeTarget;

import com.dcrux.buran.common.classes.ClassId;
import com.dcrux.buran.common.fields.IFieldSetter;
import com.dcrux.buran.common.fields.NodeFieldTarget;
import com.dcrux.buran.common.fields.getter.FieldGetClassIds;
import com.dcrux.buran.common.fields.getter.IUnfieldedDataGetter;
import com.dcrux.buran.refimpl.baseModules.BaseModule;
import com.dcrux.buran.refimpl.baseModules.nodeWrapper.CommonNode;

import java.io.Serializable;
import java.util.HashSet;

/**
 * Buran.
 *
 * @author: ${USER} Date: 09.08.13 Time: 11:49
 */
public class NodeTargetFieldPerformer {

    public Serializable performGetter(BaseModule baseModule, CommonNode node,
            NodeFieldTarget fieldTarget, IUnfieldedDataGetter<?> dataGetter) {
        switch (fieldTarget) {
            case classes:
                if (dataGetter instanceof FieldGetClassIds) {
                    FieldGetClassIds fieldGetClassIds = (FieldGetClassIds) dataGetter;
                    final HashSet<ClassId> classIds = new HashSet<>();
                    if (fieldGetClassIds.getTypes().contains(FieldGetClassIds.Type.secondaries)) {
                        classIds.addAll(node.getSecondaryClassIds());
                    }
                    if (fieldGetClassIds.getTypes().contains(FieldGetClassIds.Type.primary)) {
                        classIds.add(node.getPrimaryClassId());
                    }
                    return classIds;
                }
                break;
            default:
                throw new UnsupportedOperationException("Not yet implemeneted");
        }

        throw new IllegalArgumentException("Unsupported type");
    }

    public boolean performSetter(BaseModule base, CommonNode node, NodeFieldTarget nodeFieldTarget,
            IFieldSetter dataSetter) {
        throw new UnsupportedOperationException("Not yet implemeneted");
    }
}
