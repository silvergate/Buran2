package com.dcrux.buran.refimpl.baseModules.newIndexing.processors;

import com.dcrux.buran.common.classes.ClassId;
import com.dcrux.buran.common.query.indexingDef.ClassIdIndexingDef;
import com.dcrux.buran.common.query.queries.unfielded.ISimpleQuery;
import com.dcrux.buran.common.query.queries.unfielded.IsClass;
import com.dcrux.buran.refimpl.baseModules.newIndexing.processorsIface.FilterOrQueryBuilder;
import com.dcrux.buran.refimpl.baseModules.newIndexing.processorsIface.IIndexingDefImpl;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.QueryBuilders;

import java.io.IOException;

/**
 * Buran.
 *
 * @author: ${USER} Date: 07.08.13 Time: 18:56
 */
public class ClassIdIndexingDefImpl implements IIndexingDefImpl<ClassId, ClassIdIndexingDef> {

    @Override
    public Class<ClassIdIndexingDef> getSupportedType() {
        return ClassIdIndexingDef.class;
    }

    @Override
    public String getIndexDefSimplified(ClassIdIndexingDef intIndexingDef) {
        return "type=long, store=no, ignore_malformed=false, include_in_all=false";
    }

    @Override
    public void index(ClassIdIndexingDef indexingTypeDef, XContentBuilder contentBuilder,
            String fieldName, Object value) throws IOException {
        final ClassId classId = (ClassId) value;
        contentBuilder.field(fieldName, classId.getId());
    }

    @Override
    public FilterOrQueryBuilder build(String field,
            ISimpleQuery<ClassId, ClassIdIndexingDef> simpleQuery) {
        if (simpleQuery instanceof IsClass) {
            final IsClass isClass = (IsClass) simpleQuery;
            final long value = isClass.getRhs().getId();
            if (isClass.isConstantQuery()) {
                return FilterOrQueryBuilder
                        .filter(FilterBuilders.rangeFilter(field).from(value).to(value)
                                .includeLower(true).includeUpper(true));
            } else {
                return FilterOrQueryBuilder
                        .query(QueryBuilders.rangeQuery(field).from(value).to(value)
                                .includeLower(true).includeUpper(true));
            }
        }
        throw new IllegalArgumentException("Unknown query for this index definition");
    }

    @Override
    public FilterOrQueryBuilder buildMultifield(
            ISimpleQuery<ClassId, ClassIdIndexingDef> simpleQuery, String... fields) {
        throw new IllegalArgumentException("Multifield is not supported for int indexes");
    }

    @Override
    public int hashCode() {
        return ClassIdIndexingDefImpl.class.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return ClassIdIndexingDefImpl.class.equals(obj.getClass());
    }
}
