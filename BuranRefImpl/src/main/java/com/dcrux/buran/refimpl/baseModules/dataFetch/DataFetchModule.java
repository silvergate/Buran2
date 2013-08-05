package com.dcrux.buran.refimpl.baseModules.dataFetch;

import com.dcrux.buran.common.*;
import com.dcrux.buran.common.edges.IEdgeGetter;
import com.dcrux.buran.common.exceptions.NodeClassNotFoundException;
import com.dcrux.buran.common.exceptions.NodeNotFoundException;
import com.dcrux.buran.common.fields.IFieldGetter;
import com.dcrux.buran.common.getterSetter.BulkGet;
import com.dcrux.buran.common.getterSetter.BulkGetIndex;
import com.dcrux.buran.common.getterSetter.IBulkGetResult;
import com.dcrux.buran.common.getterSetter.IDataGetter;
import com.dcrux.buran.common.inRelations.InRealtionGetter;
import com.dcrux.buran.refimpl.baseModules.BaseModule;
import com.dcrux.buran.refimpl.baseModules.common.*;
import com.dcrux.buran.refimpl.baseModules.nodeWrapper.LiveNode;
import com.dcrux.buran.refimpl.baseModules.versions.VersionWrapper;
import com.google.common.base.Optional;
import com.orientechnologies.orient.core.id.ORID;
import com.orientechnologies.orient.core.id.ORecordId;
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

    public LiveNode getNode(Nid nid) throws NodeNotFoundException {
        final ODocument doc = getBase().getDb().load(new ORecordId(nid.getAsString()));
        if (doc == null) {
            throw new NodeNotFoundException();
        }
        final LiveNode node = new LiveNode(doc);
        return node;
    }

    public ONid toOnid(INidOrNidVer nidOrNidVer) throws NodeNotFoundException {
        if (nidOrNidVer instanceof Nid) {
            final String str = ((Nid) nidOrNidVer).getAsString();
            return new ONid(new ORecordId(str));
        } else {
            NidVer nidVer = (NidVer) nidOrNidVer;
            LiveNode liveNode = getNode(new ORecordId(((NidVer) nidOrNidVer).getAsString()));
            return liveNode.getNid();
        }
    }

    public LiveNode getNode(INidOrNidVer nidOrNidVer) throws NodeNotFoundException {
        if (nidOrNidVer instanceof Nid) {
            return getNode((Nid) nidOrNidVer);
        } else {
            NidVer nidVer = (NidVer) nidOrNidVer;
            return getNode(nidVer);
        }
    }

    public ONidVer toNidVer(Nid nid) throws NodeNotFoundException {
        final ODocument doc = getBase().getDb().load(new ORecordId(nid.getAsString()));
        if (doc == null) {
            throw NodeNotFoundException.doesNotExist();
        }
        final LiveNode incubationNode = new LiveNode(doc);
        final VersionWrapper nidVer =
                getBase().getVersionsModule().getNodeVersion(incubationNode.getNid());
        return nidVer.getONidVer();
    }

    public LiveNode getNode(NidVer nidVer) throws NodeNotFoundException {
        return getNode(new ORecordId(nidVer.getAsString()));
    }

    public LiveNode getNode(ORID versionsRecord) throws NodeNotFoundException {
        final NidVerOld nidVer = getBase().getVersionsModule().getNidVer(versionsRecord);
        if (nidVer == null) {
            throw NodeNotFoundException.deleted();
        }
        final Optional<LiveNode> node = getNode(nidVer.getNid());
        return node.get();
    }

    public LiveNode getNode(ONidVer oNidVer) throws NodeNotFoundException {
        return getNode(oNidVer.getoIdentifiable());
    }

    public void assertVersion(LiveNode node, Version version) throws NodeNotFoundException {
        if (node.isMarkedForDeletion()) {
            throw NodeNotFoundException.deleted();
        }
        Version ver = node.getVersion();
        if (ver.getVersion() != version.getVersion()) {
            throw NodeNotFoundException.wrongVersion(ver);
        }
    }

    public <TRetVal extends Serializable> TRetVal getData(ONidVer nidVer,
            IDataGetter<TRetVal> getter) throws NodeNotFoundException, NodeClassNotFoundException {
        final LiveNode node = getNode(nidVer);
        return (TRetVal) getData(node, getter);
    }

    public <TRetVal extends Serializable> Serializable getData(LiveNode node,
            IDataGetter<TRetVal> getter) throws NodeClassNotFoundException, NodeNotFoundException {
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
            return getBase().getEdgeModule().performLabelGet(node, labelGetter);
        }

        if (getter instanceof InRealtionGetter) {
            return getBase().getNewRelationsModule()
                    .processGetter(node.getOrid(), (InRealtionGetter) getter);
        }

        throw new IllegalArgumentException("Unkown data getter");
    }
}
