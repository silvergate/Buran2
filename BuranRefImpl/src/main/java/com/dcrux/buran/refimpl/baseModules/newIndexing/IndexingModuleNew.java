package com.dcrux.buran.refimpl.baseModules.newIndexing;

import com.dcrux.buran.common.UserId;
import com.dcrux.buran.common.classDefinition.ClassIndexId;
import com.dcrux.buran.common.classes.ClassId;
import com.dcrux.buran.common.exceptions.NodeClassNotFoundException;
import com.dcrux.buran.common.exceptions.NodeNotFoundException;
import com.dcrux.buran.common.fields.FieldIndex;
import com.dcrux.buran.common.fields.FieldTarget;
import com.dcrux.buran.query.IndexFieldTarget;
import com.dcrux.buran.query.IndexedFieldDef;
import com.dcrux.buran.query.IndexedFieldId;
import com.dcrux.buran.query.SingleIndexDef;
import com.dcrux.buran.query.indexingDef.IIndexingDef;
import com.dcrux.buran.query.queries.QueryTarget;
import com.dcrux.buran.refimpl.baseModules.BaseModule;
import com.dcrux.buran.refimpl.baseModules.classes.ClassDefCache;
import com.dcrux.buran.refimpl.baseModules.classes.ClassDefExt;
import com.dcrux.buran.refimpl.baseModules.common.Module;
import com.dcrux.buran.refimpl.baseModules.common.ONidVer;
import com.dcrux.buran.refimpl.baseModules.newIndexing.processorsIface.IIndexingDefImpl;
import com.dcrux.buran.refimpl.baseModules.nodeWrapper.CommonNode;
import com.dcrux.buran.refimpl.baseModules.nodeWrapper.LiveNode;
import com.orientechnologies.orient.core.id.ORID;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;

import java.io.IOException;
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

    private static final ProcessorsRegistry PROCESSORS_REGISTRY = new ProcessorsRegistry();

    public IFieldBuilder getFieldBuilder() {
        return FieldBuilder.SINGLETON;
    }

    public ProcessorsRegistry getProcessorsRegistry() {
        return PROCESSORS_REGISTRY;
    }

    private String getIndex() {
        final UserId receiver = getBase().getAuthModule().getReceiver();
        return "A" + Long.toHexString(receiver.getId());
    }

    public void removeFromIndex(UserId receiver, ORID versionsRecord, ClassId classId,
            boolean causeIsRemove) {
        final Client client = getBase().getEsModule().getClient();
        try {
        /* Add document to elasticsearch */
            final String indexName = getFieldBuilder().getIndex(receiver);
            final String typeName = getFieldBuilder().getType();
            final String id = versionsRecord.toString();
            DeleteResponse response =
                    client.prepareDelete(indexName, typeName, id).execute().actionGet();
            if (response.isNotFound()) {
                System.err.println("Error, could not delete document (not found)");
            }
        } finally {
            client.close();
        }
    }

    public void index(final UserId receiver, ONidVer versionsRecord, ClassId classId)
            throws NodeClassNotFoundException, NodeNotFoundException, IOException {
        final ClassDefExt classDefExt = getBase().getClassesModule().getClassDefExtById(classId);
        final NodeCache cache = new NodeCache();
        final CommonNode node = cache.getNodeVerLoad(getBase(), versionsRecord.getoIdentifiable());
        XContentBuilder contentBuilder = XContentFactory.jsonBuilder();
        contentBuilder.startObject();
        for (final Map.Entry<ClassIndexId, SingleIndexDef> singleIndex : classDefExt
                .getClassDefinition().getIndexesNew().getIndexes().entrySet()) {
            final ClassIndexId classIndexId = singleIndex.getKey();
            indexSingle(contentBuilder, receiver, classIndexId, singleIndex.getValue(),
                    versionsRecord, classId, classDefExt, node, cache);
        }

        contentBuilder.endObject();

        System.out.println("Added document to index: " + contentBuilder.string());

        final Client client = getBase().getEsModule().getClient();
        try {
        /* Add document to elasticsearch */
            final String indexName = getFieldBuilder().getIndex(receiver);
            final String typeName = getFieldBuilder().getType();
            IndexResponse response =
                    client.prepareIndex(indexName, typeName).setSource(contentBuilder)
                            .setId(versionsRecord.getoIdentifiable().toString()).execute()
                            .actionGet();
        } finally {
            client.close();
        }

    }

    private void buildDocumentForIndex(XContentBuilder builder, UserId receiver,
            ClassIndexId classIndexId, SingleIndexDef singleIndexDef, ClassId classId,
            Map<IndexedFieldId, Serializable> fieldValues) throws IOException {
        final ProcessorsRegistry processorRegistry = getProcessorsRegistry();
        for (final Map.Entry<IndexedFieldId, Serializable> entry : fieldValues.entrySet()) {
            final IndexedFieldId fieldIndexId = entry.getKey();
            final String fieldName = getFieldBuilder()
                    .getField(receiver, new QueryTarget(classId, classIndexId, fieldIndexId));
            IIndexingDef indexingDef =
                    singleIndexDef.getFieldDef().get(fieldIndexId).getIndexingDef();
            final IIndexingDefImpl processor = processorRegistry.getUnsafe(indexingDef.getClass());

            final Serializable value = entry.getValue();
            if (value != null) {
                /* Don't index if no value */
                processor.index(indexingDef, builder, fieldName, entry.getValue());
            }
        }
    }

    private void indexSingle(XContentBuilder builder, UserId receiver, ClassIndexId classIndexId,
            SingleIndexDef singleIndexDef, ONidVer versionsRecord, ClassId classId,
            ClassDefExt classDefExt, CommonNode node, NodeCache cache)
            throws NodeClassNotFoundException, IOException {

        /* Prepare data */
        final ClassDefCache classDefCache = new ClassDefCache();
        final Map<IndexedFieldId, Serializable> fieldValues =
                new HashMap<IndexedFieldId, Serializable>();
        for (Map.Entry<IndexedFieldId, IndexedFieldDef> entry : singleIndexDef.getFieldDef()
                .entrySet()) {
            final IndexedFieldDef fieldDef = entry.getValue();

            final IndexFieldTarget fieldTarget = fieldDef.getFieldTarget();
            if (fieldTarget.is(IndexFieldTarget.TYPE_NODE_FIELD)) {
                throw new UnsupportedOperationException("Node fields no suported at the moment");
            }

            if (fieldTarget.is(IndexFieldTarget.TYPE_FIELD_INDEX)) {
                final FieldIndex fieldIndex = fieldTarget.get(IndexFieldTarget.TYPE_FIELD_INDEX);
                final IIndexingDef<?> indexingDef = fieldDef.getIndexingDef();
                final FieldTarget fieldTargetImpl = FieldTarget.c(classId, fieldIndex);

                final Serializable value = getBase().getFieldsModule()
                        .performUnfieldedGetter((LiveNode) node, fieldTargetImpl, classDefCache,
                                indexingDef.getDataGetter());
                if ((value == null) && (indexingDef.requiredForIndex())) {
               /* Cancel, missing field, not indexable */
                    return;
                }
                fieldValues.put(entry.getKey(), value);
            }
        }

        buildDocumentForIndex(builder, receiver, classIndexId, singleIndexDef, classId,
                fieldValues);

        System.out.println("INDEXING: " + fieldValues);
    }
}
