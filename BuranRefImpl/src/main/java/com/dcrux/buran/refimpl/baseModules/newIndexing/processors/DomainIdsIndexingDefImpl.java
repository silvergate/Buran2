package com.dcrux.buran.refimpl.baseModules.newIndexing.processors;

import com.dcrux.buran.common.domain.DomainId;
import com.dcrux.buran.common.query.indexingDef.DomainIdsIndexingDef;
import com.dcrux.buran.common.query.queries.unfielded.ISimpleQuery;
import com.dcrux.buran.common.query.queries.unfielded.IsInDomain;
import com.dcrux.buran.refimpl.baseModules.newIndexing.processorsIface.FilterOrQueryBuilder;
import com.dcrux.buran.refimpl.baseModules.newIndexing.processorsIface.IIndexingDefImpl;
import com.dcrux.buran.utils.ISerSet;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermsFilterBuilder;
import org.elasticsearch.index.query.TermsQueryBuilder;

import java.io.IOException;
import java.util.Set;

/**
 * Buran.
 *
 * @author: ${USER} Date: 07.08.13 Time: 18:56
 */
public class DomainIdsIndexingDefImpl
        implements IIndexingDefImpl<ISerSet<DomainId>, DomainIdsIndexingDef> {

    @Override
    public Class<DomainIdsIndexingDef> getSupportedType() {
        return DomainIdsIndexingDef.class;
    }

    @Override
    public String getIndexDefSimplified(DomainIdsIndexingDef intIndexingDef) {
        return "type=long, store=no, ignore_malformed=false, include_in_all=false";
    }

    @Override
    public void index(DomainIdsIndexingDef indexingTypeDef, XContentBuilder contentBuilder,
            String fieldName, Object value) throws IOException {
        final ISerSet<DomainId> domainIds = (ISerSet<DomainId>) value;
        contentBuilder.startArray(fieldName);
        for (final DomainId domainId : domainIds) {
            contentBuilder.value(domainId.getId());
        }
        contentBuilder.endArray();
    }

    @Override
    public FilterOrQueryBuilder build(String field,
            ISimpleQuery<ISerSet<DomainId>, DomainIdsIndexingDef> simpleQuery) {
        if (simpleQuery instanceof IsInDomain) {
            final IsInDomain isInDomain = (IsInDomain) simpleQuery;
            final Set<DomainId> domainIds = isInDomain.getRhs();
            final long[] domainIdsLong = new long[domainIds.size()];
            int i = 0;
            for (final DomainId domainId : domainIds) {
                domainIdsLong[i] = domainId.getId();
                i++;
            }
            if (isInDomain.isConstantQuery()) {
                final TermsFilterBuilder filter = FilterBuilders.termsFilter(field, domainIdsLong);
                switch (isInDomain.getLogicOp()) {
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
                final TermsQueryBuilder query = QueryBuilders.termsQuery(field, domainIdsLong);
                switch (isInDomain.getLogicOp()) {
                    case any:
                        query.minimumMatch(1);
                        break;
                    case all:
                        query.minimumMatch(domainIds.size());
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
            ISimpleQuery<ISerSet<DomainId>, DomainIdsIndexingDef> simpleQuery, String... fields) {
        throw new IllegalArgumentException("Multifield is not supported for int indexes");
    }

    @Override
    public int hashCode() {
        return DomainIdsIndexingDefImpl.class.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return DomainIdsIndexingDefImpl.class.equals(obj.getClass());
    }
}
