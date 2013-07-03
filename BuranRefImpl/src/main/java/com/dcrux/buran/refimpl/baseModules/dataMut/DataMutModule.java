package com.dcrux.buran.refimpl.baseModules.dataMut;

import com.dcrux.buran.common.IIncNid;
import com.dcrux.buran.common.IncNodeNotFound;
import com.dcrux.buran.common.UserId;
import com.dcrux.buran.common.fields.IFieldSetter;
import com.dcrux.buran.common.getterSetter.BulkSet;
import com.dcrux.buran.common.getterSetter.IDataSetter;
import com.dcrux.buran.common.labels.ILabelSet;
import com.dcrux.buran.refimpl.baseModules.BaseModule;
import com.dcrux.buran.refimpl.baseModules.common.Module;
import com.dcrux.buran.refimpl.baseModules.nodeWrapper.IncubationNode;
import com.google.common.base.Optional;

/**
 * Buran.
 *
 * @author: ${USER} Date: 02.07.13 Time: 13:31
 */
public class DataMutModule extends Module<BaseModule> {
    public DataMutModule(BaseModule baseModule) {
        super(baseModule);
    }

    private boolean setData(final UserId sender, final IncubationNode node,
            final IDataSetter setter) {

        if (setter instanceof BulkSet) {
            final BulkSet bulkSet = (BulkSet) setter;
            boolean saveNode = false;
            for (final IDataSetter entry : bulkSet.getDataSetterSet()) {
                final boolean needNodeSave = setData(sender, node, entry);
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

        if (setter instanceof ILabelSet) {
            final ILabelSet labelSetter = (ILabelSet) setter;
            getBase().getLabelModule().performLabelSet(node, labelSetter);
            return false;
        }

        throw new IllegalArgumentException("Unknown setter type");
    }

    public void setData(UserId sender, IIncNid incNid, IDataSetter setter) throws IncNodeNotFound {
        final Optional<IncubationNode> iNode =
                getBase().getIncubationModule().getIncNode(sender, incNid);
        if (!iNode.isPresent()) {
            throw new IncNodeNotFound("Inc node not found");
        }
        final boolean needSaveNode = setData(sender, iNode.get(), setter);
        if (needSaveNode) {
            iNode.get().getDocument().save();
        }
    }
}
