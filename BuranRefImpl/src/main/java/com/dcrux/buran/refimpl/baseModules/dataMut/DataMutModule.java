package com.dcrux.buran.refimpl.baseModules.dataMut;

import com.dcrux.buran.common.IIncNid;
import com.dcrux.buran.common.IncNodeNotFound;
import com.dcrux.buran.common.UserId;
import com.dcrux.buran.common.edges.IEdgeSetter;
import com.dcrux.buran.common.exceptions.NodeClassNotFoundException;
import com.dcrux.buran.common.fields.IFieldSetter;
import com.dcrux.buran.common.getterSetter.BulkSet;
import com.dcrux.buran.common.getterSetter.IDataSetter;
import com.dcrux.buran.refimpl.baseModules.BaseModule;
import com.dcrux.buran.refimpl.baseModules.common.Module;
import com.dcrux.buran.refimpl.baseModules.fields.FieldConstraintViolationInt;
import com.dcrux.buran.refimpl.baseModules.nodeWrapper.CommonNode;
import com.dcrux.buran.refimpl.baseModules.nodeWrapper.IncubationNode;
import com.google.common.base.Optional;
import com.orientechnologies.orient.core.db.record.OIdentifiable;
import com.sun.istack.internal.Nullable;

import java.util.Set;

/**
 * Buran.
 *
 * @author: ${USER} Date: 02.07.13 Time: 13:31
 */
public class DataMutModule extends Module<BaseModule> {
    public DataMutModule(BaseModule baseModule) {
        super(baseModule);
    }

    public boolean setDataDirect(final UserId sender, final CommonNode node,
            final IDataSetter setter, @Nullable final Set<OIdentifiable> outCommittableRelations)
            throws NodeClassNotFoundException, FieldConstraintViolationInt {

        if (setter instanceof BulkSet) {
            final BulkSet bulkSet = (BulkSet) setter;
            boolean saveNode = false;
            for (final IDataSetter entry : bulkSet.getDataSetterSet()) {
                final boolean needNodeSave =
                        setDataDirect(sender, node, entry, outCommittableRelations);
                if (needNodeSave) {
                    saveNode = true;
                }
            }
            return saveNode;
        }

        if (setter instanceof IFieldSetter) {
            final IFieldSetter fieldSetter = (IFieldSetter) setter;
            getBase().getFieldsModule().performSetter(sender, node, fieldSetter);
            return true;
        }

        if (setter instanceof IEdgeSetter) {
            final IEdgeSetter labelSetter = (IEdgeSetter) setter;
            getBase().getLabelModule().performLabelSet(node, labelSetter, outCommittableRelations);
            return false;
        }

        throw new IllegalArgumentException("Unknown setter type");
    }

    private void setDataIndirect(final UserId sender, final IncubationNode node,
            final IDataSetter setter) {
        getBase().getDeltaRecorderModule().record(node, setter);
    }

    public void setData(UserId sender, IIncNid incNid, IDataSetter setter)
            throws IncNodeNotFound, NodeClassNotFoundException, FieldConstraintViolationInt {
        final Optional<IncubationNode> iNode =
                getBase().getIncubationModule().getIncNode(sender, incNid);
        if (!iNode.isPresent()) {
            throw new IncNodeNotFound("Inc node not found");
        }
        final IncubationNode node = iNode.get();

        if (node.isUpdate()) {
            setDataIndirect(sender, node, setter);
        } else {
            /* Is not an update, can set data directly */
            final boolean needSaveNode = setDataDirect(sender, node, setter, null);
            if (needSaveNode) {
                iNode.get().getDocument().save();
            }
        }
    }
}
