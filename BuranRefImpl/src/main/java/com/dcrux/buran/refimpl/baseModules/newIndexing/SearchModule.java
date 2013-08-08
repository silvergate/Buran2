package com.dcrux.buran.refimpl.baseModules.newIndexing;

import com.dcrux.buran.common.UserId;
import com.dcrux.buran.common.exceptions.NodeClassNotFoundException;
import com.dcrux.buran.query.queries.IQuery;
import com.dcrux.buran.refimpl.baseModules.BaseModule;
import com.dcrux.buran.refimpl.baseModules.classes.ClassDefCache;
import com.dcrux.buran.refimpl.baseModules.common.Module;
import com.dcrux.buran.refimpl.baseModules.newIndexing.processorsIface.FilterOrQueryBuilder;
import com.dcrux.buran.refimpl.baseModules.newIndexing.queryBuilder.QueryBuilder;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;

/**
 * Buran.
 *
 * @author: ${USER} Date: 08.08.13 Time: 20:17
 */
public class SearchModule extends Module<BaseModule> {
    public SearchModule(BaseModule baseModule) {
        super(baseModule);
    }

    public void search(UserId receiver, IQuery query) throws NodeClassNotFoundException {
        final ClassDefCache classDefCache = new ClassDefCache();
        final ProcessorsRegistry registry = getBase().getIndexingModule().getProcessorsRegistry();
        final IFieldBuilder fieldBuilder = getBase().getIndexingModule().getFieldBuilder();
        final QueryBuilder queryBuilder =
                new QueryBuilder(registry, fieldBuilder, receiver, classDefCache, getBase());

        final FilterOrQueryBuilder queryOrFilterBuilder = queryBuilder.build(query);
        if (queryOrFilterBuilder == null) {
            throw new IllegalArgumentException("No query");
        }

        final Client client = getBase().getEsModule().getClient();
        try {

            SearchRequestBuilder srb = client.prepareSearch(fieldBuilder.getIndex(receiver))
                    .setTypes(fieldBuilder.getType()).setSearchType(SearchType.QUERY_AND_FETCH)
                    .setSize(500);

            if (queryOrFilterBuilder.getQueryBuilder() != null) {
                srb.setQuery(queryOrFilterBuilder.getQueryBuilder());
                System.out
                        .println("QUERY FOR: " + queryOrFilterBuilder.getQueryBuilder().toString());
            } else {
                srb.setFilter(queryOrFilterBuilder.getFilterBuilder());
                System.out.println(
                        "QUERY FOR: " + queryOrFilterBuilder.getFilterBuilder().toString());
            }
            final SearchResponse searchResponse = srb.execute().actionGet();
            final SearchHits hits = searchResponse.getHits();
            for (final SearchHit hit : hits.getHits()) {
                System.out.println("QUERY RESULT: ID: " + hit.getId());
            }
            if (hits.getHits().length == 0) {
                System.out.println("QUERY RESULT: NONE");
            }

        } finally {
            client.close();
        }
    }

}
