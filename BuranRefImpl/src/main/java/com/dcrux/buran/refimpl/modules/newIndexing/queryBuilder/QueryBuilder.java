package com.dcrux.buran.refimpl.modules.newIndexing.queryBuilder;

import com.dcrux.buran.common.UserId;
import com.dcrux.buran.common.classDefinition.ClassDefinition;
import com.dcrux.buran.common.exceptions.NodeClassNotFoundException;
import com.dcrux.buran.common.query.IndexedFieldDef;
import com.dcrux.buran.common.query.SingleIndexDef;
import com.dcrux.buran.common.query.indexingDef.IIndexingDef;
import com.dcrux.buran.common.query.queries.IQuery;
import com.dcrux.buran.common.query.queries.QueryTarget;
import com.dcrux.buran.common.query.queries.fielded.BoolQuery;
import com.dcrux.buran.common.query.queries.fielded.IOrQueryInput;
import com.dcrux.buran.common.query.queries.fielded.MultiFieldQuery;
import com.dcrux.buran.common.query.queries.fielded.Query;
import com.dcrux.buran.refimpl.modules.BaseModule;
import com.dcrux.buran.refimpl.modules.classes.ClassDefCache;
import com.dcrux.buran.refimpl.modules.newIndexing.IFieldBuilder;
import com.dcrux.buran.refimpl.modules.newIndexing.ProcessorsRegistry;
import com.dcrux.buran.refimpl.modules.newIndexing.processorsIface.FilterOrQueryBuilder;
import com.dcrux.buran.refimpl.modules.newIndexing.processorsIface.IIndexingDefImpl;
import com.sun.istack.internal.Nullable;
import org.elasticsearch.index.query.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Buran.
 *
 * @author: ${USER} Date: 08.08.13 Time: 15:14
 */
public class QueryBuilder {

    private ProcessorsRegistry processorsRegistry;
    private IFieldBuilder fieldBuilder;
    private UserId receiver;
    private ClassDefCache classDefCache;
    private BaseModule baseModule;

    public QueryBuilder(ProcessorsRegistry processorsRegistry, IFieldBuilder fieldBuilder,
            UserId receiver, ClassDefCache classDefCache, BaseModule baseModule) {
        this.processorsRegistry = processorsRegistry;
        this.fieldBuilder = fieldBuilder;
        this.receiver = receiver;
        this.classDefCache = classDefCache;
        this.baseModule = baseModule;
    }

    @Nullable
    public FilterOrQueryBuilder build(IQuery query) throws NodeClassNotFoundException {
        if (query instanceof Query) {
            return simpleQuery((Query) query);
        } else if (query instanceof MultiFieldQuery) {
            return simpleMultiQuery((MultiFieldQuery) query);
        } else if (query instanceof BoolQuery) {
            return orQuery((BoolQuery) query);
        } else {
            throw new IllegalArgumentException("Unknown IQuery");
        }
    }

    private FilterOrQueryBuilder buildForBool(IOrQueryInput boolInput)
            throws NodeClassNotFoundException {
        return build(boolInput);
    }

    @Nullable
    private FilterOrQueryBuilder orQuery(BoolQuery boolQuery) throws NodeClassNotFoundException {
        if (boolQuery.getQueries().isEmpty()) {
            return null;
        }
        if (boolQuery.isConstantQuery()) {
            final BoolFilterBuilder filter = FilterBuilders.boolFilter();
            for (BoolQuery.Entry element : boolQuery.getQueries()) {
                final FilterBuilder filterBuilder =
                        toFilter(buildForBool(element.getOrQueryInput()));
                switch (element.getLogic()) {
                    case must:
                        filter.must(filterBuilder);
                        break;
                    case mustNot:
                        filter.mustNot(filterBuilder);
                        break;
                    case should:
                        filter.should(filterBuilder);
                        break;
                    default:
                        throw new IllegalArgumentException("Unknown logic op");
                }
            }
            return FilterOrQueryBuilder.filter(filter);
        } else {
            final BoolQueryBuilder query = QueryBuilders.boolQuery();
            for (BoolQuery.Entry element : boolQuery.getQueries()) {
                final org.elasticsearch.index.query.QueryBuilder queryFilter =
                        toQuery(buildForBool(element.getOrQueryInput()));
                switch (element.getLogic()) {
                    case must:
                        query.must(queryFilter);
                        break;
                    case mustNot:
                        query.mustNot(queryFilter);
                        break;
                    case should:
                        query.should(queryFilter);
                        break;
                    default:
                        throw new IllegalArgumentException("Unknown logic op");
                }
            }
            return FilterOrQueryBuilder.query(query);
        }
    }

    private FilterBuilder toFilter(FilterOrQueryBuilder filterOrQueryBuilder) {
        if (filterOrQueryBuilder.getQueryBuilder() == null) {
            return filterOrQueryBuilder.getFilterBuilder();
        } else {
            return FilterBuilders.queryFilter(filterOrQueryBuilder.getQueryBuilder());
        }
    }

    public static org.elasticsearch.index.query.QueryBuilder toQuery(
            FilterOrQueryBuilder filterOrQueryBuilder) {
        if (filterOrQueryBuilder.getQueryBuilder() == null) {
            return QueryBuilders.constantScoreQuery(filterOrQueryBuilder.getFilterBuilder());
        } else {
            return filterOrQueryBuilder.getQueryBuilder();
        }
    }

    private IIndexingDef<?> getIndexingDef(QueryTarget target) throws NodeClassNotFoundException {
        final ClassDefinition classDef =
                this.classDefCache.getClassDef(this.baseModule, target.getClassId());
        final SingleIndexDef indexDef =
                classDef.getIndexesNew().getIndexes().get(target.getIndexId());
        final IndexedFieldDef fieldInIndex = indexDef.getFieldDef().get(target.getIndexedFieldId());
        final IIndexingDef<?> indexingDef = fieldInIndex.getIndexingDef();
        return indexingDef;
    }

    private FilterOrQueryBuilder simpleMultiQuery(MultiFieldQuery multiFieldQuery)
            throws NodeClassNotFoundException {
        final Set<IIndexingDef<?>> indexingDefs = new HashSet<>();
        String[] fields = new String[multiFieldQuery.getIndexedFieldIds().size()];
        int index = 0;
        for (QueryTarget entry : multiFieldQuery.getIndexedFieldIds()) {
            indexingDefs.add(getIndexingDef(entry));
            fields[index] = this.fieldBuilder.getField(this.receiver, entry);
            index++;
        }
        if (indexingDefs.size() > 1) {
            throw new IllegalArgumentException(
                    "Fileds in multifield queries must be all of the " + "same index type.");
        }
        if (indexingDefs.isEmpty()) {
            throw new IllegalArgumentException("No targets in index field.");
        }
        final IIndexingDef<?> indexingDef2 = indexingDefs.iterator().next();

        final IIndexingDefImpl<Serializable, IIndexingDef<Serializable>> processor =
                (IIndexingDefImpl<Serializable, IIndexingDef<Serializable>>) this.processorsRegistry
                        .get(indexingDef2.getClass());

        final FilterOrQueryBuilder queryOrFilter =
                processor.buildMultifield(multiFieldQuery.getDef(), fields);
        return queryOrFilter;
    }

    private FilterOrQueryBuilder simpleQuery(Query query) throws NodeClassNotFoundException {
        final QueryTarget target = query.getTarget();
        final String fieldName = this.fieldBuilder.getField(this.receiver, target);

        final IIndexingDef<?> indexingDef2 = getIndexingDef(target);
        final IIndexingDefImpl processor =
                (IIndexingDefImpl) this.processorsRegistry.get(indexingDef2.getClass());

        final FilterOrQueryBuilder queryOrFilter = processor.build(fieldName, query.getDef());
        return queryOrFilter;
    }
}
