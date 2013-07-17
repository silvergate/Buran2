package com.dcrux.buran.refimpl.baseModules.notifications;

import com.dcrux.buran.callbackCommands.CbComNodeCommit;
import com.dcrux.buran.common.NidVer;
import com.dcrux.buran.common.classDefinition.ClassIndexName;
import com.dcrux.buran.common.classes.ClassId;
import com.dcrux.buran.common.subscription.SubBlockId;
import com.dcrux.buran.common.subscription.SubId;
import com.dcrux.buran.refimpl.baseModules.BaseModule;
import com.dcrux.buran.refimpl.baseModules.common.Module;
import com.dcrux.buran.refimpl.baseModules.index.eval.EvaluatedMap;
import com.dcrux.buran.refimpl.subscription.subRegistry.SubRegistry;
import com.google.common.collect.Multimap;
import com.orientechnologies.orient.core.id.ORID;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Buran.
 *
 * @author: ${USER} Date: 10.07.13 Time: 00:50
 */
public class NotificationsModule extends Module<BaseModule> {
    public NotificationsModule(BaseModule baseModule) {
        super(baseModule);
    }

    public void notifyAddedToIndex(ClassId classId, ORID versionsRecord,
            Map<ClassIndexName, Collection<EvaluatedMap>> evalResult) {
        System.out.println(MessageFormat
                .format("notifyAddedToIndex: {0}, {1}, {2}", classId, versionsRecord, evalResult));
        final SubRegistry registry =
                getBase().getSubscriptionModule().getRegistry(getBase().getReceiver());
        if (registry == null) {
            return;
        }

        /* Return only unique block and sub-id entries */
        final Set<BlockAndSubId> blockAndSubIds = new HashSet<>();
        for (final Map.Entry<ClassIndexName, Collection<EvaluatedMap>> cniEntry : evalResult
                .entrySet()) {
            final ClassIndexName classIndexName = cniEntry.getKey();
            for (final EvaluatedMap evaluatedMap : cniEntry.getValue()) {
                final Multimap<SubBlockId, SubId> entryMap =
                        registry.getByMapValue(classId, classIndexName, evaluatedMap.getKey());
                if (entryMap != null) {
                    for (Map.Entry<SubBlockId, SubId> entry : entryMap.entries()) {
                        blockAndSubIds.add(new BlockAndSubId(entry.getKey(), entry.getValue()));
                    }
                }
            }
        }

        /* Emit callback commands */
        for (BlockAndSubId blockAndSubId : blockAndSubIds) {
            getBase().emitCallbackCommand(
                    new CbComNodeCommit(blockAndSubId.getBlockId(), blockAndSubId.getSubId(),
                            new NidVer(versionsRecord.toString())));
        }
    }

    public void removedOrUpdated(ClassId classId, ORID versionsRecord, boolean removed) {
        System.out.println(
                MessageFormat.format("removedOrUpdated: {0}, {1}", classId, versionsRecord));
    }
}
