package com.dcrux.buran.refimpl.baseModules.newIndexing.processorsIface;

import com.sun.istack.internal.Nullable;
import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.QueryBuilder;

/**
 * Buran.
 *
 * @author: ${USER} Date: 08.08.13 Time: 14:26
 */
public class FilterOrQueryBuilder {

    private final FilterBuilder filterBuilder;
    private final QueryBuilder queryBuilder;

    private FilterOrQueryBuilder(FilterBuilder filterBuilder, QueryBuilder queryBuilder) {
        this.filterBuilder = filterBuilder;
        this.queryBuilder = queryBuilder;
    }

    public static FilterOrQueryBuilder filter(FilterBuilder filterBuilder) {
        return new FilterOrQueryBuilder(filterBuilder, null);
    }

    public static FilterOrQueryBuilder query(QueryBuilder queryBuilder) {
        return new FilterOrQueryBuilder(null, queryBuilder);
    }

    @Nullable
    public FilterBuilder getFilterBuilder() {
        return filterBuilder;
    }

    @Nullable
    public QueryBuilder getQueryBuilder() {
        return queryBuilder;
    }
}
