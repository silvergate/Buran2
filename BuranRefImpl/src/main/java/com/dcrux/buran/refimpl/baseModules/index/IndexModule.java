package com.dcrux.buran.refimpl.baseModules.index;

import com.dcrux.buran.common.classes.ClassId;
import com.dcrux.buran.common.exceptions.NodeClassNotFoundException;
import com.dcrux.buran.common.exceptions.NodeNotFoundException;
import com.dcrux.buran.refimpl.baseModules.BaseModule;
import com.dcrux.buran.refimpl.baseModules.common.Module;
import com.dcrux.buran.refimpl.baseModules.index.eval.MapFunEvaluator;
import com.dcrux.buran.refimpl.baseModules.index.functionCompiler.FunctionCompiler;
import com.orientechnologies.orient.core.id.ORID;

/**
 * Buran.
 *
 * @author: ${USER} Date: 04.07.13 Time: 14:33
 */
public class IndexModule extends Module<BaseModule> {

    private final FunctionCompiler functionCompiler = new FunctionCompiler(getBase());
    private final MapFunEvaluator mapFunEvaluator = new MapFunEvaluator(getBase());
    private final IndexImpl indexImpl = new IndexImpl(getBase());

    public IndexModule(BaseModule baseModule) {
        super(baseModule);
    }

    public FunctionCompiler getFunctionCompiler() {
        return functionCompiler;
    }

    public MapFunEvaluator getMapFunEvaluator() {
        return mapFunEvaluator;
    }

    public IndexImpl getIndexImpl() {
        return indexImpl;
    }

    public void removeFromIndex(ORID versionsRecord, ClassId classId) {
        System.out.println("   - REMOVE FROM INDEX: " + versionsRecord);
    }

    public void index(ORID versionsRecord, ClassId classId)
            throws NodeNotFoundException, NodeClassNotFoundException {
        getIndexImpl().index(versionsRecord, classId);
        System.out.println("   - ADD TO INDEX: " + versionsRecord);
    }

    /*public IChangeTracker createChangeTracker() {
        return new IChangeTracker() {
            @Override
            public void newNode(LiveNode newNode) {
                System.out.println("  CHANGE TRACKER: New Node");
            }

            @Override
            public void updatedNode(LiveNode updatedNode) {
                System.out.println("  CHANGE TRACKER: Updated Node");
            }

            @Override
            public void removedNode(LiveNode deletedNode) {
                System.out.println("  CHANGE TRACKER: Removed Node");
            }

            @Override
            public void newRelation(LiveNode sourceNode, RelationWrapper relation) {
                System.out.println("  CHANGE TRACKER: New Relation: " +
                        relation.getDocument().getIdentity().toString());
            }

            @Override
            public void removedRelation(LiveNode sourceNode, RelationWrapper relation) {
                System.out.println("  CHANGE TRACKER: Removed relation");
            }

            @Override
            public void fieldUpdate(Time time, FieldIndex fieldIndex, CommonNode sourceNode) {
                System.out.println("  CHANGE TRACKER: Updated FIELD " + fieldIndex + ", " +
                        "time: " + time);
            }
        };
    }      */
}
