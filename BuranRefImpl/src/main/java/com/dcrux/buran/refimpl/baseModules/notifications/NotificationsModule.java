package com.dcrux.buran.refimpl.baseModules.notifications;

import com.dcrux.buran.common.classDefinition.ClassIndexName;
import com.dcrux.buran.common.classes.ClassId;
import com.dcrux.buran.refimpl.baseModules.BaseModule;
import com.dcrux.buran.refimpl.baseModules.common.Module;
import com.dcrux.buran.refimpl.baseModules.index.eval.EvaluatedMap;
import com.orientechnologies.orient.core.id.ORID;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.Map;

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
    }

    public void removedOrUpdated(ClassId classId, ORID versionsRecord, boolean removed) {
        System.out.println(
                MessageFormat.format("removedOrUpdated: {0}, {1}", classId, versionsRecord));
    }
}