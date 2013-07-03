package com.dcrux.buran.refimpl.baseModules.dataFetch;

import com.dcrux.buran.common.INid;
import com.dcrux.buran.common.NidVer;
import com.dcrux.buran.common.Version;
import com.dcrux.buran.common.edges.IEdgeGetter;
import com.dcrux.buran.common.exceptions.NodeClassNotFoundException;
import com.dcrux.buran.common.exceptions.NodeNotFoundException;
import com.dcrux.buran.common.fields.IFieldGetter;
import com.dcrux.buran.common.getterSetter.BulkGet;
import com.dcrux.buran.common.getterSetter.BulkGetIndex;
import com.dcrux.buran.common.getterSetter.IBulkGetResult;
import com.dcrux.buran.common.getterSetter.IDataGetter;
import com.dcrux.buran.refimpl.baseModules.BaseModule;
import com.dcrux.buran.refimpl.baseModules.common.IfaceUtils;
import com.dcrux.buran.refimpl.baseModules.common.Module;
import com.dcrux.buran.refimpl.baseModules.common.ONid;
import com.dcrux.buran.refimpl.baseModules.nodeWrapper.LiveNode;
import com.google.common.base.Optional;
import com.orientechnologies.orient.core.record.impl.ODocument;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Buran.
 *
 * @author: ${USER} Date: 29.06.13 Time: 13:59
 */
public class DataFetchModule extends Module<BaseModule> {

    public DataFetchModule(BaseModule baseModule) {
        super(baseModule);
    }

    public Optional<LiveNode> getNode(INid nid) {
        final ONid oNid = IfaceUtils.getONid(nid);
        final ODocument doc = getBase().getDb().load(oNid.getRecordId());
        final LiveNode incubationNode = new LiveNode(doc);
        return Optional.of(incubationNode);
    }

    public void assertVersion(LiveNode node, Version version) throws NodeNotFoundException {
        Version ver = node.getVersion();
        if (ver.getVersion() != version.getVersion()) {
            throw NodeNotFoundException.wrongVersion(ver);
        }
    }

    public Optional<LiveNode> getNode(final NidVer nidVer) throws NodeNotFoundException {
        final ONid oNid = IfaceUtils.getONid(nidVer.getNid());

        final Optional<LiveNode> nodeStored = getNode(oNid);
        if (!nodeStored.isPresent()) {
            return nodeStored;
        }
        assertVersion(nodeStored.get(), nidVer.getVersion());
        return nodeStored;
    }

    public LiveNode getNodeReq(final NidVer nidVer) throws NodeNotFoundException {
        final Optional<LiveNode> nodeOpt = getNode(nidVer);
        if (nodeOpt.isPresent()) {
            return nodeOpt.get();
        }
        throw NodeNotFoundException.doesNotExist();
    }

    public <TRetVal extends Serializable> TRetVal getData(NidVer nidVer,
            IDataGetter<TRetVal> getter) throws NodeNotFoundException, NodeClassNotFoundException {
        final LiveNode node = getNodeReq(nidVer);
        return (TRetVal) getData(node, getter);
    }

    public <TRetVal extends Serializable> Serializable getData(LiveNode node,
            IDataGetter<TRetVal> getter) throws NodeClassNotFoundException {
        if (getter instanceof BulkGet) {
            final BulkGet bulkGet = (BulkGet) getter;
            final List<Serializable> results = new ArrayList<>();
            for (final IDataGetter<?> entry : bulkGet.getDataGetterList()) {
                results.add(getData(node, entry));
            }
            return (TRetVal) new IBulkGetResult() {
                @Override
                public <TRetVal> TRetVal get(BulkGetIndex<TRetVal> index) {
                    return (TRetVal) results.get(index.getIndex());
                }
            };
        }

        /* Field data */
        if (getter instanceof IFieldGetter) {
            final IFieldGetter fieldGetter = (IFieldGetter) getter;
            return (TRetVal) getBase().getFieldsModule().performGetter(node, fieldGetter);
        }

        if (getter instanceof IEdgeGetter) {
            final IEdgeGetter labelGetter = (IEdgeGetter) getter;
            return getBase().getLabelModule().performLabelGet(node, labelGetter);
        }

        throw new IllegalArgumentException("Unkown data getter");
    }
}
