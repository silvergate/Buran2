package com.dcrux.buran.refimpl.modules.newIndexing;

import com.dcrux.buran.commands.indexingNew.QueryResultNew;
import com.dcrux.buran.common.NidVer;
import com.dcrux.buran.common.UserId;
import com.dcrux.buran.common.exceptions.NodeClassNotFoundException;
import com.dcrux.buran.common.query.queries.IQuery;
import com.dcrux.buran.refimpl.modules.BaseModule;
import com.dcrux.buran.refimpl.modules.classes.ClassDefCache;
import com.dcrux.buran.refimpl.modules.common.Module;
import com.dcrux.buran.refimpl.modules.newIndexing.processorsIface.FilterOrQueryBuilder;
import com.dcrux.buran.refimpl.modules.newIndexing.queryBuilder.QueryBuilder;
import com.dcrux.buran.utils.SerList;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;

import java.util.ArrayList;
import java.util.List;

/**
 * Buran.
 *
 * @author: ${USER} Date: 08.08.13 Time: 20:17
 */
public class SearchModule extends Module<BaseModule> {
    public SearchModule(BaseModule baseModule) {
        super(baseModule);
    }

    public FilterOrQueryBuilder buildQuery(UserId receiver, IQuery query)
            throws NodeClassNotFoundException {
        final ClassDefCache classDefCache = new ClassDefCache();
        final ProcessorsRegistry registry = getBase().getIndexingModule().getProcessorsRegistry();
        final IFieldBuilder fieldBuilder = getBase().getIndexingModule().getFieldBuilder();
        final QueryBuilder queryBuilder =
                new QueryBuilder(registry, fieldBuilder, receiver, classDefCache, getBase());

        final FilterOrQueryBuilder queryOrFilterBuilder = queryBuilder.build(query);
        if (queryOrFilterBuilder == null) {
            throw new IllegalArgumentException("No query");
        }
        return queryOrFilterBuilder;
    }

    public QueryResultNew search(UserId receiver, IQuery query) throws NodeClassNotFoundException {
        final IFieldBuilder fieldBuilder = getBase().getIndexingModule().getFieldBuilder();

        final FilterOrQueryBuilder queryOrFilterBuilder = buildQuery(receiver, query);

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

            final List<NidVer> results = new ArrayList<NidVer>();
            for (final SearchHit hit : hits.getHits()) {
                //System.out.println("QUERY RESULT: ID: " + hit.getId());
                results.add(new NidVer(hit.getId()));
            }
            if (hits.getHits().length == 0) {
                System.out.println("QUERY RESULT: NONE");
            }

            return new QueryResultNew(false, SerList.wrap(results));
        } finally {
            client.close();
        }
    }

}
