package com.dcrux.buran.refimpl.baseModules.elasticSearch;

import com.dcrux.buran.common.UserId;
import com.dcrux.buran.refimpl.baseModules.BaseModule;
import com.dcrux.buran.refimpl.baseModules.common.Module;
import com.dcrux.buran.refimpl.baseModules.newIndexing.FieldBuilder;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.io.FileSystemUtils;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;

import java.io.File;

/**
 * Buran.
 *
 * @author: ${USER} Date: 07.08.13 Time: 11:02
 */
public class EsModule extends Module<BaseModule> {

    private Node node;

    public void deleteData() {
        deleteDirectory();
    }

    public EsModule(BaseModule baseModule) {
        super(baseModule);

        ImmutableSettings.Builder esSettings =
                ImmutableSettings.settingsBuilder().put("http" + ".enabled", "false")
                        .put("path.data", "/Users/caelis/esTest");
        this.node = NodeBuilder.nodeBuilder().local(true).settings(esSettings.build()).node();
    }

    public void ensureIndex(UserId receiver) {
        final String index = FieldBuilder.getIndexStatic(receiver);

        final Client client = getClient();
        try {
            final IndicesExistsResponse response =
                    client.admin().indices().prepareExists(index).execute().actionGet();
            if (!response.isExists()) {
                client.admin().indices().prepareCreate(index).execute().actionGet();
            }
        } finally {
            client.close();
        }
    }

    public void shutdown() {
        this.node.close();
    }

    public Client getClient() {
        return this.node.client();
    }

    private void deleteDirectory() {
        FileSystemUtils.deleteRecursively(new File("/Users/caelis/esTest"));
    }

}
