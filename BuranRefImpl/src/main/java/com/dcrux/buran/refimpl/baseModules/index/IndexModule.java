package com.dcrux.buran.refimpl.baseModules.index;

import com.dcrux.buran.common.classDefinition.ClassIndexDefinition;
import com.dcrux.buran.common.classes.ClassId;
import com.dcrux.buran.common.exceptions.NodeClassNotFoundException;
import com.dcrux.buran.common.exceptions.NodeNotFoundException;
import com.dcrux.buran.refimpl.baseModules.BaseModule;
import com.dcrux.buran.refimpl.baseModules.common.Module;
import com.dcrux.buran.refimpl.baseModules.index.eval.MapFunEvaluator;
import com.dcrux.buran.refimpl.baseModules.index.functionCompiler.FunctionCompiler;
import com.dcrux.buran.refimpl.baseModules.index.functionCompiler.IndexingAdditionalInfo;
import com.dcrux.buran.refimpl.baseModules.index.keyGen.KeyGenModule;
import com.dcrux.buran.refimpl.baseModules.index.mapIndex.MapIndexModule;
import com.dcrux.buran.refimpl.baseModules.index.orientSerializer.BuranKeySerializer;
import com.orientechnologies.orient.core.id.ORID;
import com.orientechnologies.orient.core.serialization.serializer.binary.OBinarySerializerFactory;

/**
 * Buran.
 *
 * @author: ${USER} Date: 04.07.13 Time: 14:33
 */
public class IndexModule extends Module<BaseModule> {

    private final FunctionCompiler functionCompiler = new FunctionCompiler(getBase());
    private final MapFunEvaluator mapFunEvaluator = new MapFunEvaluator(getBase());
    private final IndexImpl indexImpl = new IndexImpl(getBase());
    private final MapIndexModule mapIndexModule = new MapIndexModule(getBase());
    private final KeyGenModule keyGenModule = new KeyGenModule(getBase());

    public IndexModule(BaseModule baseModule) {
        super(baseModule);
    }

    public void setupDb() {
        OBinarySerializerFactory.INSTANCE.registerSerializer(new BuranKeySerializer(), null);
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

    public KeyGenModule getKeyGenModule() {
        return keyGenModule;
    }

    public void removeFromIndex(ORID versionsRecord, ClassId classId, boolean causeIsRemove)
            throws NodeClassNotFoundException {
        System.out.println("   - REMOVE FROM INDEX: " + versionsRecord);
        getIndexImpl().removeFromIndexAndNotify(versionsRecord, classId, causeIsRemove);
    }

    public void index(ORID versionsRecord, ClassId classId)
            throws NodeNotFoundException, NodeClassNotFoundException {
        getIndexImpl().indexAndNotify(versionsRecord, classId);
        System.out.println("   - ADD TO INDEX: " + versionsRecord);
    }

    public IndexingAdditionalInfo prepareClassForIndexing(ClassId classId,
            ClassIndexDefinition cid) {
        getMapIndexModule().createIndexes(classId, cid);
        return new IndexingAdditionalInfo(
                getFunctionCompiler().compileAndValidateMapFunctions(cid));
    }

    public MapIndexModule getMapIndexModule() {
        return mapIndexModule;
    }

}
