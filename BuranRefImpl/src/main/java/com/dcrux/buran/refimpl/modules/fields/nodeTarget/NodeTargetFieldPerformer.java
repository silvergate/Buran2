package com.dcrux.buran.refimpl.modules.fields.nodeTarget;

import com.dcrux.buran.common.classes.ClassId;
import com.dcrux.buran.common.domain.DomainId;
import com.dcrux.buran.common.fields.IFieldSetter;
import com.dcrux.buran.common.fields.NodeFieldTarget;
import com.dcrux.buran.common.fields.getter.FieldGetClassIds;
import com.dcrux.buran.common.fields.getter.FieldGetDomainIds;
import com.dcrux.buran.common.fields.getter.IUnfieldedDataGetter;
import com.dcrux.buran.common.fields.setter.FieldDomMod;
import com.dcrux.buran.refimpl.modules.BaseModule;
import com.dcrux.buran.refimpl.modules.nodeWrapper.CommonNode;
import com.dcrux.buran.utils.SerSet;

import java.io.Serializable;
import java.text.MessageFormat;
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
            case domains:
                if (dataGetter instanceof FieldGetDomainIds) {
                    return SerSet.wrap(node.getDomainIds());
                }
                break;
            default:
                throw new UnsupportedOperationException("Not yet implemeneted");
        }

        throw new IllegalArgumentException("Unsupported type");
    }

    public boolean performSetter(BaseModule base, CommonNode node, NodeFieldTarget nodeFieldTarget,
            IFieldSetter dataSetter) {
        switch (nodeFieldTarget) {
            case domains:
                if (dataSetter instanceof FieldDomMod) {
                    final FieldDomMod fieldDomMod = (FieldDomMod) dataSetter;
                    switch (fieldDomMod.getType()) {
                        case add:
                            /* Domains must exist on adding */
                            for (final DomainId domId : fieldDomMod.getDomainIds()) {
                                final boolean exists = base.getDomainsModule().exists(domId);
                                if (!exists) {
                                    throw new IllegalArgumentException(MessageFormat
                                            .format("Domain {0} does not exist.", domId));
                                }
                                node.addDomainId(domId);
                            }
                            break;
                        case remove:
                            /* No checks are performed on removal */
                            for (final DomainId domId : fieldDomMod.getDomainIds()) {
                                node.removeDomainId(domId);
                            }
                            break;
                    }
                }
                break;
            default:
                throw new UnsupportedOperationException("Not yet implemeneted");
        }
        throw new UnsupportedOperationException("Not yet implemeneted");
    }
}
