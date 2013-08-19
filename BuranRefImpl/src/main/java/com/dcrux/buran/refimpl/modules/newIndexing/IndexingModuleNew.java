package com.dcrux.buran.refimpl.modules.newIndexing;

import com.dcrux.buran.common.UserId;
import com.dcrux.buran.common.classDefinition.ClassDefinition;
import com.dcrux.buran.common.classDefinition.ClassIndexId;
import com.dcrux.buran.common.classes.ClassId;
import com.dcrux.buran.common.exceptions.NodeClassNotFoundException;
import com.dcrux.buran.common.exceptions.NodeNotFoundException;
import com.dcrux.buran.common.fields.FieldIndex;
import com.dcrux.buran.common.fields.FieldTarget;
import com.dcrux.buran.common.fields.NodeFieldTarget;
import com.dcrux.buran.common.fields.getter.SingleGet;
import com.dcrux.buran.common.query.IndexFieldTarget;
import com.dcrux.buran.common.query.IndexedFieldDef;
import com.dcrux.buran.common.query.IndexedFieldId;
import com.dcrux.buran.common.query.SingleIndexDef;
import com.dcrux.buran.common.query.indexingDef.IIndexingDef;
import com.dcrux.buran.common.query.queries.QueryTarget;
import com.dcrux.buran.refimpl.modules.BaseModule;
import com.dcrux.buran.refimpl.modules.classes.ClassDefCache;
import com.dcrux.buran.refimpl.modules.classes.ClassDefExt;
import com.dcrux.buran.refimpl.modules.common.Module;
import com.dcrux.buran.refimpl.modules.common.ONidVer;
import com.dcrux.buran.refimpl.modules.newIndexing.processorsIface.IIndexingDefImpl;
import com.dcrux.buran.refimpl.modules.nodeWrapper.CommonNode;
import com.dcrux.buran.refimpl.modules.nodeWrapper.LiveNode;
import com.orientechnologies.orient.core.id.ORID;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequestBuilder;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;

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

    private void indexSingleClassId(UserId receiver, ONidVer versionsRecord,
            XContentBuilder contentBuilder, ClassDefCache classDefCache, ClassId classId,
            NodeCache nodeCache)
            throws NodeClassNotFoundException, NodeNotFoundException, IOException {
        final ClassDefExt classDefExt = classDefCache.getClassDefExt(getBase(), classId);
        final CommonNode node =
                nodeCache.getNodeVerLoad(getBase(), versionsRecord.getoIdentifiable());
        for (final Map.Entry<ClassIndexId, SingleIndexDef> singleIndex : classDefExt
                .getClassDefinition().getIndexesNew().getIndexes().entrySet()) {
            final ClassIndexId classIndexId = singleIndex.getKey();
            indexSingle(contentBuilder, receiver, classIndexId, singleIndex.getValue(),
                    versionsRecord, classId, classDefExt, node, nodeCache);
        }
    }

    public void index(final UserId receiver, ONidVer versionsRecord, ClassId primaryClassId)
            throws NodeClassNotFoundException, NodeNotFoundException, IOException {
        final NodeCache nodeCache = new NodeCache();
        final ClassDefCache classDefCache = new ClassDefCache();
        final CommonNode node =
                nodeCache.getNodeVerLoad(getBase(), versionsRecord.getoIdentifiable());
        final Set<ClassId> allClasses = node.getAllClassIds();

        XContentBuilder contentBuilder = XContentFactory.jsonBuilder();
        contentBuilder.startObject();
        for (final ClassId classId : allClasses) {
            indexSingleClassId(receiver, versionsRecord, contentBuilder, classDefCache, classId,
                    nodeCache);
        }
        contentBuilder.endObject();

        System.out.println("Added document to index: " + contentBuilder.string());

        final Client client = getBase().getEsModule().getClient();
        IndexResponse response;
        try {
        /* Add document to elasticsearch */
            final String indexName = getFieldBuilder().getIndex(receiver);
            final String typeName = getFieldBuilder().getType();
            response = client.prepareIndex(indexName, typeName).setSource(contentBuilder)
                    .setId(versionsRecord.getoIdentifiable().toString()).setPercolate("*").execute()
                    .actionGet();

        } finally {
            client.close();
        }

        /* Percolation matches */
        final List<String> percolationMatches = response.getMatches();
        if (percolationMatches != null) {
            for (final String percolationId : percolationMatches) {
                System.out.println("PERCOLATION MATCH: " + percolationId);
            }
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
            final IIndexingDef<?> indexingDef = fieldDef.getIndexingDef();
            final IndexFieldTarget fieldTarget = fieldDef.getFieldTarget();
            if (fieldTarget.is(IndexFieldTarget.TYPE_NODE_FIELD)) {
                final NodeFieldTarget nodeFieldTarget =
                        fieldTarget.get(IndexFieldTarget.TYPE_NODE_FIELD);
                final SingleGet singleGet =
                        SingleGet.c(nodeFieldTarget, indexingDef.getDataGetter());
                final Serializable value =
                        getBase().getFieldsModule().performGetter((LiveNode) node, singleGet);
                if ((value == null) && (indexingDef.requiredForIndex())) {
               /* Cancel, missing field, not indexable */
                    return;
                }
                fieldValues.put(entry.getKey(), value);
            } else {

                if (fieldTarget.is(IndexFieldTarget.TYPE_FIELD_INDEX)) {
                    final FieldIndex fieldIndex =
                            fieldTarget.get(IndexFieldTarget.TYPE_FIELD_INDEX);
                    final FieldTarget fieldTargetImpl = FieldTarget.c(classId, fieldIndex);

                    final Serializable value = getBase().getFieldsModule()
                            .performUnfieldedGetter((LiveNode) node, fieldTargetImpl, classDefCache,
                                    indexingDef.getDataGetter());
                    if ((value == null) && (indexingDef.requiredForIndex())) {
               /* Cancel, missing field, not indexable */
                        return;
                    }
                    fieldValues.put(entry.getKey(), value);
                } else {
                    throw new IllegalArgumentException("Unknwo fiel index type");
                }
            }
        }

        buildDocumentForIndex(builder, receiver, classIndexId, singleIndexDef, classId,
                fieldValues);
    }

    public void prepareForIndexing(UserId receiver, ClassId classId,
            ClassDefinition classDefinition) throws IOException {
        //TODO: Das scheint nicht zu funktionieren

        final IFieldBuilder fieldBuilder = getFieldBuilder();
        final String index = fieldBuilder.getIndex(receiver);

        //final XContentBuilder contentBuilder = XContentFactory.jsonBuilder();
        //contentBuilder.startObject();
        final List<String> elements = new ArrayList<>();
        for (final Map.Entry<ClassIndexId, SingleIndexDef> singleIndexDef : classDefinition
                .getIndexesNew().getIndexes().entrySet()) {
            for (final Map.Entry<IndexedFieldId, IndexedFieldDef> indexedField : singleIndexDef
                    .getValue().getFieldDef().entrySet()) {
                final IndexedFieldDef def = indexedField.getValue();
                final IIndexingDefImpl defImpl =
                        getProcessorsRegistry().get(def.getIndexingDef().getClass());
                final String defStr = defImpl.getIndexDefSimplified(def.getIndexingDef());
                final QueryTarget queryTarget =
                        new QueryTarget(classId, singleIndexDef.getKey(), indexedField.getKey());
                final String fieldName = fieldBuilder.getField(receiver, queryTarget);
                //contentBuilder.field(fieldName, defStr);
                elements.add(fieldName);
                elements.add(defStr);
            }
        }
        //contentBuilder.endObject();

        final String type = fieldBuilder.getType();

        final Client client = getBase().getEsModule().getClient();
        try {

            final PutMappingRequestBuilder putMapping =
                    client.admin().indices().preparePutMapping(index).setIgnoreConflicts(false)
                            .setSource(elements.toArray()).setType(type);
            final PutMappingResponse response = putMapping.execute().actionGet();

            //System.out.println("PutMapping. ACK=" + putMapping.request().toString());
            //System.out.println("PutMappingResponse. ACK=" + response.isAcknowledged());
        } finally {
            client.close();
        }
    }
}