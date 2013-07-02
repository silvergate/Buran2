package com.dcrux.buran.refimpl.dao;

import com.dcrux.buran.*;
import com.dcrux.buran.fields.BatchSet;
import com.dcrux.buran.fields.FieldIndex;
import com.dcrux.buran.fields.setter.IDataSetter;
import com.dcrux.buran.labels.ILabelSet;
import com.dcrux.buran.refimpl.IfaceUtils;
import com.dcrux.buran.refimpl.baseModules.BaseModule;
import com.dcrux.buran.refimpl.model.CommitInfo;
import com.dcrux.buran.refimpl.model.IncubationNode;
import com.dcrux.buran.refimpl.model.LiveNode;
import com.dcrux.buran.refimpl.model.ONid;
import com.google.common.base.Optional;
import com.orientechnologies.orient.core.db.document.ODatabaseDocument;
import com.orientechnologies.orient.core.record.impl.ODocument;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Buran.
 *
 * @author: ${USER} Date: 29.06.13 Time: 13:59
 */
public class IncubationSrv {

    private final BaseModule impl;

    public IncubationSrv(BaseModule impl) {
        this.impl = impl;
    }

    public ITransRet<IIncNid> createInc(final UserId sender, final ClassId classId) {
        impl.getClasses().assureIncClass(classId);
        return new ITransRet<IIncNid>() {
            @Override
            public IIncNid run(ODatabaseDocument db, IRunner runner) {
                final IncubationNode incubationNode = IncubationNode
                        .createNew(classId, impl.getCurrentTimestampProvider().get(), sender);
                incubationNode.getDocument().save();
                return incubationNode.getNid();
            }
        };
    }

    ;

    public ITransRet<Optional<IncubationNode>> getIncNode(final UserId sender, IIncNid nid) {
        final ONid oIncNid = IfaceUtils.getOincNid(nid);
        return new ITransRet<Optional<IncubationNode>>() {
            @Override
            public Optional<IncubationNode> run(ODatabaseDocument db, IRunner runner) {
                final ODocument doc = db.load(oIncNid.getRecordId());
                final IncubationNode incubationNode = new IncubationNode(doc);
                if (!sender.equals(incubationNode.getSender())) {
                    /* Wrong sender, nothing found */
                    return Optional.absent();
                }
                if (incubationNode.isLive()) {
                    /* Not incubation node */
                    return Optional.absent();
                }

                return Optional.of(incubationNode);
            }
        };
    }

    public ITransaction setData(final UserId sender, final IIncNid incNid, final BatchSet batchSet,
            final Set<ILabelSet> labelSetters) {
        return new ITransaction() {
            @Override
            public void run(final ODatabaseDocument db, IRunner runner) throws Throwable {
                final Optional<IncubationNode> iNode =
                        runner.reuse().run(getIncNode(sender, incNid));
                if (!iNode.isPresent()) {
                    throw new IncNodeNotFound("Inc node not found");
                }

                /* Set data */
                final IncubationNode node = iNode.get();
                for (final FieldIndex fieldIndex : batchSet.getSetterMap().keySet()) {
                    final IDataSetter value = batchSet.getSetterMap().get(fieldIndex);
                    impl.getGetterSetterSrv().performSetter(sender, node, fieldIndex, value);
                }

                /* Set label */
                for (final ILabelSet labelSetter : labelSetters) {
                    runner.reuse().run(impl.getLabelSrv().performLabelSet(node, labelSetter));
                }

                node.getDocument().save();
            }
        };
    }

    public ITransRet<LiveNode> commitNode(final IncubationNode incubationNode) {
        return new ITransRet<LiveNode>() {
            @Override
            public LiveNode run(ODatabaseDocument db, IRunner runner) throws Throwable {
                final boolean update = incubationNode.getUpdateVersion() != null;
                if (!update) {
                    incubationNode.goLive();
                    final LiveNode storeNode = new LiveNode(incubationNode.getDocument());
                    storeNode.setVersion(Version.INITIAL);
                    incubationNode.getDocument().save();
                    return storeNode;
                } else {
                    final ONid upOnid = incubationNode.getUpdateNid();
                    final Version upVersion = incubationNode.getUpdateVersion();
                    LiveNode up = runner.reuse()
                            .run(impl.getDataSrv().getNodeReq(new NidVer(upOnid, upVersion)));

                    /* Update node */
                    if (up.getClassId().getId() != incubationNode.getClassId().getId()) {
                        throw new IllegalStateException("Incompatible class");
                    }
                    /* Remove fields */
                    for (final String upFieldName : up.getDocument().fieldNames()) {
                        up.getDocument().removeField(upFieldName);
                    }
                    for (final String fieldName : incubationNode.getDocument().fieldNames()) {
                        final Object value = incubationNode.getDocument().field(fieldName);
                        System.out.println("Transferring " + fieldName + ", value = " + value);
                        up.getDocument().field(fieldName, value);
                    }

                    up.incVersion();
                    up.getDocument().save();

                    return up;
                }
            }
        };
    }

    public ITransRet<Map<IIncNid, NidVer>> commit(final UserId sender,
            final Collection<IIncNid> incNids) {
        return new ITransRet<Map<IIncNid, NidVer>>() {
            @Override
            public Map<IIncNid, NidVer> run(final ODatabaseDocument db, IRunner runner)
                    throws Throwable {
                final Map<IIncNid, NidVer> result = new HashMap<>();
                final CommitInfo commitInfo = new CommitInfo();

                /* Commit node */
                for (final IIncNid incNid : incNids) {
                    final Optional<IncubationNode> iNode =
                            runner.reuse().run(getIncNode(sender, incNid));
                    if (!iNode.isPresent()) {
                        throw new IncNodeNotFound("Inc node not found");
                    }
                    final IncubationNode node = iNode.get();
                    final LiveNode liveNode = runner.reuse().run(commitNode(node));
                    result.put(incNid, liveNode.getNidVer());
                    commitInfo.add(new CommitInfo.CommitEntry(node, liveNode));
                }

                /* Commit label */
                for (CommitInfo.CommitEntry entry : commitInfo.getCommitEntrySet()) {
                    impl.getLabelSrv()
                            .commit(entry.getIncubationNode(), entry.getLiveNode(), commitInfo,
                                    runner.reuse());
                }

                /* Remove incubation */
                for (CommitInfo.CommitEntry entry : commitInfo.getCommitEntrySet()) {
                    if (entry.isUpdate()) {
                        runner.reuse().run(removeIncNode(
                                new ONid(entry.getIncubationNode().getNid().getRecordId())));
                    }
                }

                return result;
            }
        };
    }

    public ITransaction removeIncNode(final ONid incNid) {
        return new ITransaction() {
            @Override
            public void run(ODatabaseDocument db, IRunner runner) throws Throwable {
                // TODO: Alle felder und so zeugs muss auch weg.
                db.delete(incNid.getRecordId());
            }
        };
    }

}
