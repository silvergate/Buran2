package com.dcrux.buran.refimpl.modules.subscription;

import com.dcrux.buran.common.UserId;
import com.dcrux.buran.common.exceptions.NodeClassNotFoundException;
import com.dcrux.buran.common.query.queries.IQuery;
import com.dcrux.buran.common.subscription.SubBlockId;
import com.dcrux.buran.common.subscription.SubId;
import com.dcrux.buran.refimpl.modules.BaseModule;
import com.dcrux.buran.refimpl.modules.common.Module;
import com.dcrux.buran.refimpl.modules.newIndexing.IFieldBuilder;
import com.dcrux.buran.refimpl.modules.newIndexing.processorsIface.FilterOrQueryBuilder;
import com.dcrux.buran.refimpl.modules.newIndexing.queryBuilder.QueryBuilder;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.deletebyquery.DeleteByQueryResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.ConstantScoreQueryBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermFilterBuilder;

import java.io.IOException;
import java.text.MessageFormat;

/**
 * Buran.
 *
 * @author: ${USER} Date: 17.07.13 Time: 01:49
 */
public class SubscriptionModule extends Module<BaseModule> {
    public SubscriptionModule(BaseModule baseModule) {
        super(baseModule);
    }

    public static final String INDEX_PERCOLATOR = "_percolator";
    public static final String FIELD_BLOCK_ID = "blockId";

    private String generatePercolatorId(SubBlockId blockId, SubId subId) {
        return MessageFormat.format("{0}-{1}", blockId.getId(), Long.toHexString(subId.getId()));
    }

    public void register(UserId receiver, SubBlockId blockId, SubId subId, IQuery query)
            throws NodeClassNotFoundException, IOException {
        final FilterOrQueryBuilder queryOrFilter =
                getBase().getSearchModule().buildQuery(receiver, query);
        final org.elasticsearch.index.query.QueryBuilder qb = QueryBuilder.toQuery(queryOrFilter);
        IFieldBuilder fb = getBase().getIndexingModule().getFieldBuilder();
        final String index = fb.getIndex(receiver);
        final String percolatorId = generatePercolatorId(blockId, subId);

        final Client client = getBase().getEsModule().getClient();
        try {
            /* Remove old percolator, if any */
            final DeleteResponse deleteResponse =
                    client.prepareDelete(INDEX_PERCOLATOR, index, percolatorId).setRefresh(true)
                            .execute().actionGet();
            if (!deleteResponse.isNotFound()) {
                System.out.println("Removed old percolator");
            }
            /* Add new percolator */
            final XContentBuilder src =
                    XContentFactory.jsonBuilder().startObject().field("query", qb)
                            .field(FIELD_BLOCK_ID, blockId.getId()).endObject();
            client.prepareIndex(INDEX_PERCOLATOR, index, percolatorId).setSource(src)
                    .setRefresh(true).execute().actionGet();
            System.out.println("Percolator DEF: " + src.string());
        } finally {
            client.close();
        }
    }

    public boolean removeSubscription(UserId receiver, SubBlockId blockId, SubId subId) {
        final String percolatorId = generatePercolatorId(blockId, subId);
        IFieldBuilder fb = getBase().getIndexingModule().getFieldBuilder();
        final String index = fb.getIndex(receiver);

        final Client client = getBase().getEsModule().getClient();
        try {
            /* Remove old percolator, if any */
            final DeleteResponse deleteResponse =
                    client.prepareDelete(INDEX_PERCOLATOR, index, percolatorId).setRefresh(true)
                            .execute().actionGet();
            if (deleteResponse.isNotFound()) {
                return false;
            } else {
                return true;
            }
        } finally {
            client.close();
        }
    }

    public void removeBlock(UserId receiver, SubBlockId blockId) {
        IFieldBuilder fb = getBase().getIndexingModule().getFieldBuilder();
        final String index = fb.getIndex(receiver);

        final Client client = getBase().getEsModule().getClient();
        try {
            /* Remove percolators */
            final TermFilterBuilder filter =
                    FilterBuilders.termFilter(FIELD_BLOCK_ID, blockId.getId());
            final ConstantScoreQueryBuilder query = QueryBuilders.constantScoreQuery(filter);
            final DeleteByQueryResponse response =
                    client.prepareDeleteByQuery(INDEX_PERCOLATOR).setTypes(index).setQuery(query)
                            .execute().actionGet();
        } finally {
            client.close();
        }
    }


}
