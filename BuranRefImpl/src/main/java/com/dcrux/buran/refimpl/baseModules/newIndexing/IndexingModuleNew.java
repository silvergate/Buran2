package com.dcrux.buran.refimpl.baseModules.newIndexing;

import com.dcrux.buran.common.classDefinition.ClassIndexNameNew;
import com.dcrux.buran.common.classes.ClassId;
import com.dcrux.buran.common.exceptions.NodeClassNotFoundException;
import com.dcrux.buran.common.exceptions.NodeNotFoundException;
import com.dcrux.buran.common.fields.FieldIndex;
import com.dcrux.buran.common.fields.IFieldTarget;
import com.dcrux.buran.queryDsl.IndexedFieldDef;
import com.dcrux.buran.queryDsl.IndexedFieldId;
import com.dcrux.buran.queryDsl.SingleIndexDef;
import com.dcrux.buran.queryDsl.indexingDef.IIndexingDef;
import com.dcrux.buran.refimpl.baseModules.BaseModule;
import com.dcrux.buran.refimpl.baseModules.classes.ClassDefExt;
import com.dcrux.buran.refimpl.baseModules.common.Module;
import com.dcrux.buran.refimpl.baseModules.common.ONidVer;
import com.dcrux.buran.refimpl.baseModules.nodeWrapper.CommonNode;
import com.dcrux.buran.refimpl.baseModules.nodeWrapper.LiveNode;
import com.orientechnologies.orient.core.id.ORID;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Buran.
 *
 * @author: ${USER} Date: 06.08.13 Time: 23:11
 */
public class IndexingModuleNew extends Module<BaseModule> {

    public IndexingModuleNew(BaseModule baseModule) {
        super(baseModule);
    }

    public void removeFromIndex(ORID versionsRecord, ClassId classId, boolean causeIsRemove) {
    }

    public void index(ONidVer versionsRecord, ClassId classId)
            throws NodeClassNotFoundException, NodeNotFoundException {
        final ClassDefExt classDefExt = getBase().getClassesModule().getClassDefExtById(classId);
        final NodeCache cache = new NodeCache();
        final CommonNode node = cache.getNodeVerLoad(getBase(), versionsRecord.getoIdentifiable());
        for (final Map.Entry<ClassIndexNameNew, SingleIndexDef> singleIndex : classDefExt
                .getClassDefinition().getIndexesNew().getIndexes().entrySet()) {
            indexSingle(singleIndex.getKey(), singleIndex.getValue(), versionsRecord, classId,
                    classDefExt, node, cache);
        }
    }

    private void indexSingle(ClassIndexNameNew classIndexName, SingleIndexDef singleIndexDef,
            ONidVer versionsRecord, ClassId classId, ClassDefExt classDefExt, CommonNode node,
            NodeCache cache) {

        /* Prepare data */
        final Map<IndexedFieldId, Serializable> fieldValues =
                new HashMap<IndexedFieldId, Serializable>();
        for (Map.Entry<IndexedFieldId, IndexedFieldDef> entry : singleIndexDef.getFieldDef()
                .entrySet()) {
            final IndexedFieldDef fieldDef = entry.getValue();
            if (fieldDef.getDependencyIndex().isPresent()) {
                throw new UnsupportedOperationException("Not yet implemented!");
            }

            final IFieldTarget fieldTarget = fieldDef.getFieldTarget();
            if (!(fieldTarget instanceof FieldIndex)) {
                throw new UnsupportedOperationException("Only FieldIndexes suported at the moment");
            }

            final IIndexingDef<Serializable> indexingDef = fieldDef.getIndexingDef();

            final Serializable value = getBase().getFieldsModule()
                    .performUnfieldedGetter((LiveNode) node, classDefExt.getClassDefinition(),
                            (FieldIndex) fieldTarget, indexingDef.getDataGetter());
            if ((value == null) && (indexingDef.requiredForIndex())) {
               /* Cancel, missing field, not indexable */
                return;
            }
            fieldValues.put(entry.getKey(), value);
        }


    }
}
