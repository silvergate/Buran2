package com.dcrux.buran.refimpl.baseModules.newIndexing.processors;

import com.dcrux.buran.common.classes.ClassId;
import com.dcrux.buran.query.indexingDef.ClassIdsIndexingDef;
import com.dcrux.buran.query.queries.unfielded.HasClass;
import com.dcrux.buran.query.queries.unfielded.ISimpleQuery;
import com.dcrux.buran.refimpl.baseModules.newIndexing.processorsIface.FilterOrQueryBuilder;
import com.dcrux.buran.refimpl.baseModules.newIndexing.processorsIface.IIndexingDefImpl;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermsFilterBuilder;
import org.elasticsearch.index.query.TermsQueryBuilder;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Buran.
 *
 * @author: ${USER} Date: 07.08.13 Time: 18:56
 */
public class ClassIdsIndexingDefImpl
        implements IIndexingDefImpl<HashSet<ClassId>, ClassIdsIndexingDef> {

    @Override
    public Class<ClassIdsIndexingDef> getSupportedType() {
        return ClassIdsIndexingDef.class;
    }

    @Override
    public String getIndexDefSimplified(ClassIdsIndexingDef intIndexingDef) {
        return "type=long, store=no, ignore_malformed=false, include_in_all=false";
    }

    @Override
    public void index(ClassIdsIndexingDef indexingTypeDef, XContentBuilder contentBuilder,
            String fieldName, Object value) throws IOException {
        final HashSet<ClassId> classIds = (HashSet<ClassId>) value;
        contentBuilder.startArray(fieldName);
        for (final ClassId classId : classIds) {
            contentBuilder.value(classId.getId());
        }
        contentBuilder.endArray();
    }

    @Override
    public FilterOrQueryBuilder build(String field,
            ISimpleQuery<HashSet<ClassId>, ClassIdsIndexingDef> simpleQuery) {
        if (simpleQuery instanceof HasClass) {
            final HasClass hasClass = (HasClass) simpleQuery;
            final Set<ClassId> classIds = hasClass.getRhs();
            final long[] classIdsLong = new long[classIds.size()];
            int i = 0;
            for (final ClassId classId : classIds) {
                classIdsLong[i] = classId.getId();
                i++;
            }
            if (hasClass.isConstantQuery()) {
                final TermsFilterBuilder filter = FilterBuilders.termsFilter(field, classIdsLong);
                switch (hasClass.getLogicOp()) {
                    case any:
                        break;
                    case all:
                        filter.execution("and");
                        break;
                    default:
                        throw new IllegalArgumentException("Unknown op");
                }
                return FilterOrQueryBuilder.filter(filter);
            } else {
                final TermsQueryBuilder query = QueryBuilders.termsQuery(field, classIdsLong);
                switch (hasClass.getLogicOp()) {
                    case any:
                        query.minimumMatch(1);
                        break;
                    case all:
                        query.minimumMatch(classIds.size());
                        break;
                    default:
                        throw new IllegalArgumentException("Unknown op");
                }
                return FilterOrQueryBuilder.query(query);
            }
        }
        throw new IllegalArgumentException("Unknown query for this index definition");
    }

    @Override
    public FilterOrQueryBuilder buildMultifield(
            ISimpleQuery<HashSet<ClassId>, ClassIdsIndexingDef> simpleQuery, String... fields) {
        throw new IllegalArgumentException("Multifield is not supported for int indexes");
    }

    @Override
    public int hashCode() {
        return ClassIdsIndexingDefImpl.class.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return ClassIdsIndexingDefImpl.class.equals(obj.getClass());
    }
}
