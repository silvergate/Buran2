package com.dcrux.buran.refimpl.modules.newIndexing.processors;

import com.dcrux.buran.common.query.indexingDef.IntIndexingDef;
import com.dcrux.buran.common.query.queries.unfielded.ISimpleQuery;
import com.dcrux.buran.common.query.queries.unfielded.IntCmp;
import com.dcrux.buran.refimpl.modules.newIndexing.processorsIface.FilterOrQueryBuilder;
import com.dcrux.buran.refimpl.modules.newIndexing.processorsIface.IIndexingDefImpl;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import java.io.IOException;

/**
 * Buran.
 *
 * @author: ${USER} Date: 07.08.13 Time: 18:56
 */
public class IntIndexingDefImpl implements IIndexingDefImpl<Number, IntIndexingDef> {

    @Override
    public Class<IntIndexingDef> getSupportedType() {
        return IntIndexingDef.class;
    }

    @Override
    public String getIndexDefSimplified(IntIndexingDef intIndexingDef) {
        return "type=long, store=no, ignore_malformed=false, include_in_all=false";
    }

    @Override
    public void index(IntIndexingDef indexingTypeDef, XContentBuilder contentBuilder,
            String fieldName, Object value) throws IOException {
        final Number number = (Number) value;
        contentBuilder.field(fieldName, number.longValue());
    }

    @Override
    public FilterOrQueryBuilder build(String field,
            ISimpleQuery<Number, IntIndexingDef> simpleQuery) {
        if (simpleQuery instanceof IntCmp) {
            final IntCmp intCmp = (IntCmp) simpleQuery;
            final long value = intCmp.getRhs().longValue();
            final FilterBuilder filter;
            final QueryBuilder query;
            switch (intCmp.getCmp()) {
                case equal:
                    if (intCmp.isConstantQuery()) {
                        filter = FilterBuilders.rangeFilter(field).from(value).to(value)
                                .includeLower(true).includeUpper(true);
                        query = null;
                    } else {
                        query = QueryBuilders.rangeQuery(field).from(value).to(value)
                                .includeLower(true).includeUpper(true);
                        filter = null;
                    }
                    break;
                case greater:
                    if (intCmp.isConstantQuery()) {
                        filter = FilterBuilders.rangeFilter(field).gt(value);
                        query = null;
                    } else {
                        query = QueryBuilders.rangeQuery(field).gt(value);
                        filter = null;
                    }
                    break;
                case less:
                    if (intCmp.isConstantQuery()) {
                        filter = FilterBuilders.rangeFilter(field).lt(value);
                        query = null;
                    } else {
                        query = QueryBuilders.rangeQuery(field).lt(value);
                        filter = null;
                    }
                    break;
                default:
                    throw new IllegalArgumentException("Unknown comparator");
            }
            if (filter != null) {
                return FilterOrQueryBuilder.filter(filter);
            } else {
                return FilterOrQueryBuilder.query(query);
            }
        }
        throw new IllegalArgumentException("Unknown query for this index definition");
    }

    @Override
    public FilterOrQueryBuilder buildMultifield(ISimpleQuery<Number, IntIndexingDef> simpleQuery,
            String... fields) {
        throw new IllegalArgumentException("Multifield is not supported for int indexes");
    }

    @Override
    public int hashCode() {
        return IntIndexingDefImpl.class.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return IntIndexingDefImpl.class.equals(obj.getClass());
    }
}
