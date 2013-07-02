package com.dcrux.buran.refimpl.baseModules.dataMut;

import com.dcrux.buran.common.IIncNid;
import com.dcrux.buran.common.IncNodeNotFound;
import com.dcrux.buran.common.UserId;
import com.dcrux.buran.common.fields.IFieldSetter;
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

    public void setData(final UserId sender, final IIncNid incNid,
            final Optional<IFieldSetter> dataSetter, final Optional<ILabelSet> labelSetter)
            throws IncNodeNotFound {
        final Optional<IncubationNode> iNode =
                getBase().getIncubationModule().getIncNode(sender, incNid);
        if (!iNode.isPresent()) {
            throw new IncNodeNotFound("Inc node not found");
        }

        final IncubationNode node = iNode.get();

                /* Set data */
        if (dataSetter.isPresent()) {
            //for (final FieldIndex fieldIndex : batchSet.getSetterMap().keySet()) {
            //final IFieldSetter value = batchSet.getSetterMap().get(fieldIndex);
            getBase().getFieldsModule().performSetter(sender, node, dataSetter.get());
            //}
            node.getDocument().save();
        }

                /* Set label */
        if (labelSetter.isPresent()) {
            getBase().getLabelModule().performLabelSet(node, labelSetter.get());
        }

    }


}
