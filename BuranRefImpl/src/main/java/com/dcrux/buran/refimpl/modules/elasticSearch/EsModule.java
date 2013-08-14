package com.dcrux.buran.refimpl.modules.elasticSearch;

import com.dcrux.buran.common.UserId;
import com.dcrux.buran.refimpl.modules.BaseModule;
import com.dcrux.buran.refimpl.modules.newIndexing.FieldBuilder;
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
public class EsModule {

    private static Node node;
    private Node nodeThis;

    public static void deleteAllEsData() {
        FileSystemUtils.deleteRecursively(getEsDirectory());
    }

    public static File getEsDirectory() {
        final File buranHome = BaseModule.getBuranHome();
        final File esHome = new File(buranHome, "elastic_v0");
        if (!esHome.exists()) {
            esHome.mkdirs();
        }
        return esHome;
    }

    public static synchronized Node getNode() {
        if (node == null) {
            ImmutableSettings.Builder esSettings =
                    ImmutableSettings.settingsBuilder().put("http" + ".enabled", "false")
                            .put("path.data", getEsDirectory().getAbsolutePath());
            node = NodeBuilder.nodeBuilder().local(true).settings(esSettings.build()).node();
        }
        return node;
    }

    public EsModule() {
        this.nodeThis = getNode();
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

    public void removeUserIndex(UserId receiver) {
        final String index = FieldBuilder.getIndexStatic(receiver);

        final Client client = getClient();
        try {
            final IndicesExistsResponse response =
                    client.admin().indices().prepareExists(index).execute().actionGet();
            if (response.isExists()) {
                client.admin().indices().prepareDelete(index).execute().actionGet();
            }
        } finally {
            client.close();
        }
    }

    public void shutdown() {
        this.nodeThis.close();
    }

    public Client getClient() {
        return this.nodeThis.client();
    }
}
