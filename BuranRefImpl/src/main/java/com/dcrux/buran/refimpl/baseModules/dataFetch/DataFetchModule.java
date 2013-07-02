package com.dcrux.buran.refimpl.baseModules.dataFetch;

import com.dcrux.buran.commands.dataFetch.FetchResult;
import com.dcrux.buran.common.INid;
import com.dcrux.buran.common.NidVer;
import com.dcrux.buran.common.NodeNotFoundException;
import com.dcrux.buran.common.Version;
import com.dcrux.buran.common.fields.IDataGetter;
import com.dcrux.buran.common.labels.ILabelGet;
import com.dcrux.buran.refimpl.baseModules.BaseModule;
import com.dcrux.buran.refimpl.baseModules.common.IfaceUtils;
import com.dcrux.buran.refimpl.baseModules.common.Module;
import com.dcrux.buran.refimpl.baseModules.common.ONid;
import com.dcrux.buran.refimpl.baseModules.nodeWrapper.LiveNode;
import com.google.common.base.Optional;
import com.orientechnologies.orient.core.record.impl.ODocument;

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
            throw new NodeNotFoundException("Wrong node version", true);
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
        throw new NodeNotFoundException("Node not found", false);
    }

    public <T extends Object> T performLabelGet(final NidVer nidVer, final ILabelGet<T> labelGet)
            throws NodeNotFoundException {
        //TODO: Mergen mit 'getData()'
        final Optional<LiveNode> nodeOpt = getNode(nidVer);
        if (!nodeOpt.isPresent()) {
            throw new NodeNotFoundException("Node not found", false);
        }
        final LiveNode node = nodeOpt.get();
        return getBase().getLabelModule().performLabelGet(node, labelGet);
    }

    public FetchResult getData(final NidVer nidVer, final Optional<IDataGetter> fieldGetter,
            final Optional<ILabelGet> labelGetter) throws NodeNotFoundException {
        final Optional<LiveNode> nodeOpt = getNode(nidVer);
        if (!nodeOpt.isPresent()) {
            throw new NodeNotFoundException("Node not found", false);
        }

        final LiveNode node = nodeOpt.get();

        /* Field data */
        Optional<Object> fieldResult;
        if (fieldGetter.isPresent()) {
            fieldResult = Optional.fromNullable(
                    getBase().getFieldsModule().performGetter(node, fieldGetter.get()));
        } else {
            fieldResult = Optional.absent();
        }

        /* Label data */
        Optional<Object> labelResult;
        if (labelGetter.isPresent()) {
            labelResult = Optional.fromNullable(
                    getBase().getLabelModule().performLabelGet(node, labelGetter.get()));
        } else {
            labelResult = Optional.absent();
        }

        return new FetchResult(fieldResult, labelResult);
    }
}
