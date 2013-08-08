package com.dcrux.buran.refimpl.baseModules.nodes;

import com.dcrux.buran.common.UserId;
import com.dcrux.buran.common.exceptions.NodeClassNotFoundException;
import com.dcrux.buran.common.fields.setter.FieldRemoveAll;
import com.dcrux.buran.common.nodes.INodeSetter;
import com.dcrux.buran.common.nodes.setter.ClassIdMut;
import com.dcrux.buran.common.nodes.setter.NodeRemove;
import com.dcrux.buran.refimpl.baseModules.BaseModule;
import com.dcrux.buran.refimpl.baseModules.common.Module;
import com.dcrux.buran.refimpl.baseModules.fields.FieldConstraintViolationInt;
import com.dcrux.buran.refimpl.baseModules.nodeWrapper.CommonNode;
import com.dcrux.buran.refimpl.baseModules.nodeWrapper.LiveNode;

/**
 * Buran.
 *
 * @author: ${USER} Date: 10.07.13 Time: 19:17
 */
public class NodesModule extends Module<BaseModule> {
    public NodesModule(BaseModule baseModule) {
        super(baseModule);
    }

    private void perform_classIdMut(UserId sender, CommonNode node, ClassIdMut classIdMut)
            throws NodeClassNotFoundException, FieldConstraintViolationInt {
        switch (classIdMut.getType()) {
            case add:
                node.addClass(classIdMut.getClassId());
                break;
            case remove:
                /* Remove all fields */
                FieldRemoveAll fieldRemoveAll = new FieldRemoveAll(classIdMut.getClassId());
                getBase().getFieldsModule().performSetter(sender, node, fieldRemoveAll);
                /* Now remove class */
                node.removeClass(classIdMut.getClassId());
                break;
            default:
                throw new IllegalArgumentException("Unknown type");
        }
    }

    public void performNodeSet(UserId sender, CommonNode node, INodeSetter nodeSetter)
            throws NodeClassNotFoundException, FieldConstraintViolationInt {
        if (nodeSetter instanceof NodeRemove) {
            final LiveNode liveNode = new LiveNode(node.getDocument());
            liveNode.markForDeletion();
        } else if (nodeSetter instanceof ClassIdMut) {
            final ClassIdMut classIdMut = (ClassIdMut) nodeSetter;
            perform_classIdMut(sender, node, classIdMut);
        } else {
            throw new IllegalArgumentException("Unknown node setter");
        }
    }
}
