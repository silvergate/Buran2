package com.dcrux.buran.refimpl.dao;

import com.dcrux.buran.INid;
import com.dcrux.buran.NidVer;
import com.dcrux.buran.NodeNotFoundException;
import com.dcrux.buran.Version;
import com.dcrux.buran.fields.BatchGet;
import com.dcrux.buran.fields.BatchGetResult;
import com.dcrux.buran.fields.FieldIndex;
import com.dcrux.buran.fields.getter.IDataGetter;
import com.dcrux.buran.labels.ILabelGet;
import com.dcrux.buran.refimpl.IfaceUtils;
import com.dcrux.buran.refimpl.baseModules.BaseModule;
import com.dcrux.buran.refimpl.model.LiveNode;
import com.dcrux.buran.refimpl.model.ONid;
import com.google.common.base.Optional;
import com.orientechnologies.orient.core.db.document.ODatabaseDocument;
import com.orientechnologies.orient.core.record.impl.ODocument;

/**
 * Buran.
 *
 * @author: ${USER} Date: 29.06.13 Time: 13:59
 */
public class DataSrv {

    private final BaseModule impl;

    public DataSrv(BaseModule impl) {
        this.impl = impl;
    }

    public ITransRet<Optional<LiveNode>> getNode(INid nid) {
        final ONid oNid = IfaceUtils.getONid(nid);
        return new ITransRet<Optional<LiveNode>>() {
            @Override
            public Optional<LiveNode> run(ODatabaseDocument db, IRunner runner) {
                final ODocument doc = db.load(oNid.getRecordId());
                final LiveNode incubationNode = new LiveNode(doc);
                return Optional.of(incubationNode);
            }
        };
    }

    public void assertVersion(LiveNode node, Version version) throws Exception {
        Version ver = node.getVersion();
        if (ver.getVersion() != version.getVersion()) {
            throw new NodeNotFoundException("Wrong node version", true);
        }
    }

    public ITransRet<Optional<LiveNode>> getNode(final NidVer nidVer) {
        final ONid oNid = IfaceUtils.getONid(nidVer.getNid());
        return new ITransRet<Optional<LiveNode>>() {
            @Override
            public Optional<LiveNode> run(ODatabaseDocument db, IRunner runner) throws Throwable {
                final Optional<LiveNode> nodeStored = runner.reuse().run(getNode(oNid));
                if (!nodeStored.isPresent()) {
                    return nodeStored;
                }
                assertVersion(nodeStored.get(), nidVer.getVersion());
                return nodeStored;
            }
        };
    }

    public ITransRet<LiveNode> getNodeReq(final NidVer nidVer) {
        return new ITransRet<LiveNode>() {
            @Override
            public LiveNode run(ODatabaseDocument db, IRunner runner) throws Throwable {
                final Optional<LiveNode> nodeOpt = runner.reuse().run(getNode(nidVer));
                if (nodeOpt.isPresent()) {
                    return nodeOpt.get();
                }
                throw new NodeNotFoundException("Node not found", false);
            }
        };
    }

    public <T extends Object> ITransRet<T> performLabelGet(final NidVer nidVer,
            final ILabelGet<T> labelGet) {
        //TODO: Mergen mit 'getData()'
        return new ITransRet<T>() {
            @Override
            public T run(ODatabaseDocument db, IRunner runner) throws Throwable {
                final Optional<LiveNode> nodeOpt = runner.reuse().run(getNode(nidVer));
                if (!nodeOpt.isPresent()) {
                    throw new NodeNotFoundException("Node not found", false);
                }
                final LiveNode node = nodeOpt.get();
                return runner.reuse().run(impl.getLabelSrv().performLabelGet(node, labelGet));
            }
        };
    }

    public ITransRet<BatchGetResult> getData(final NidVer nidVer, final BatchGet getters) {
        return new ITransRet<BatchGetResult>() {
            @Override
            public BatchGetResult run(ODatabaseDocument db, IRunner runner) throws Throwable {
                final Optional<LiveNode> nodeOpt = runner.reuse().run(getNode(nidVer));
                if (!nodeOpt.isPresent()) {
                    throw new NodeNotFoundException("Node not found", false);
                }
                final LiveNode node = nodeOpt.get();
                final BatchGetResult result = new BatchGetResult();
                for (final FieldIndex index : getters.getEntries().keySet()) {
                    final IDataGetter<?> getter = getters.getEntries().get(index);
                    final Object value =
                            impl.getGetterSetterSrv().performGetter(node, index, getter);
                    result.getValues().put(index, value);
                }
                return result;
            }
        };
    }
}
