package com.dcrux.buran.refimpl.baseModules.index;

import com.dcrux.buran.common.classDefinition.ClassDefinition;
import com.dcrux.buran.common.classDefinition.ClassIndexDefinition;
import com.dcrux.buran.common.classDefinition.ClassIndexName;
import com.dcrux.buran.common.classes.ClassId;
import com.dcrux.buran.common.exceptions.NodeClassNotFoundException;
import com.dcrux.buran.common.exceptions.NodeNotFoundException;
import com.dcrux.buran.refimpl.baseModules.BaseModule;
import com.dcrux.buran.refimpl.baseModules.classes.ClassDefExt;
import com.dcrux.buran.refimpl.baseModules.common.Module;
import com.dcrux.buran.refimpl.baseModules.index.eval.EvaluatedMap;
import com.dcrux.buran.refimpl.baseModules.nodeWrapper.LiveNode;
import com.orientechnologies.orient.core.id.ORID;

import java.util.Collection;
import java.util.Map;

/**
 * Buran.
 *
 * @author: ${USER} Date: 08.07.13 Time: 10:49
 */
public class IndexImpl extends Module<BaseModule> {

    public IndexImpl(BaseModule baseModule) {
        super(baseModule);
    }

    private ClassIndexDefinition getIndexDef(ClassId classId) throws NodeClassNotFoundException {
        final ClassDefinition classDef = getBase().getClassesModule().getClassDefById(classId);
        return classDef.getIndexes();
    }

    public void removeFromIndex(ORID versionsRecord, ClassId classId)
            throws NodeClassNotFoundException {
        final ClassDefExt classDefExt = getBase().getClassesModule().getClassDefExtById(classId);
        getBase().getIndexModule().getMapIndexModule()
                .removeFromMapStorage(classId, classDefExt, versionsRecord);
    }

    public void index(ORID versionsRecord, ClassId classId)
            throws NodeNotFoundException, NodeClassNotFoundException {
        final LiveNode node = getBase().getDataFetchModule().getNode(versionsRecord);
        final ClassDefExt classDefExt =
                getBase().getClassesModule().getClassDefExtById(node.getClassId());

        /* Eval values for map index */
        final Map<ClassIndexName, Collection<EvaluatedMap>> evalResult =
                getBase().getIndexModule().getMapFunEvaluator().eval(classDefExt, node);

        /* Store in map index */
        getBase().getIndexModule().getMapIndexModule()
                .addToMapStorage(classId, classDefExt, versionsRecord, evalResult);

        System.out.println("INDEX Eval Result: " + evalResult);
    }


}
