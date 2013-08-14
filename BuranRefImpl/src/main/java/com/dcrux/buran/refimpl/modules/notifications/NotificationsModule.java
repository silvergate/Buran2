package com.dcrux.buran.refimpl.modules.notifications;

import com.dcrux.buran.common.classes.ClassId;
import com.dcrux.buran.refimpl.modules.BaseModule;
import com.dcrux.buran.refimpl.modules.common.Module;
import com.orientechnologies.orient.core.id.ORID;

import java.text.MessageFormat;

/**
 * Buran.
 *
 * @author: ${USER} Date: 10.07.13 Time: 00:50
 */
public class NotificationsModule extends Module<BaseModule> {
    public NotificationsModule(BaseModule baseModule) {
        super(baseModule);
    }

    public void notifyAddedToIndex(ClassId classId, ORID versionsRecord) {
        //TODO: Implement ME

        System.out.println(
                MessageFormat.format("notifyAddedToIndex: {0}, {1}, ", classId, versionsRecord));


        /* Emit callback commands */
            /*getBase().emitCallbackCommand(
                    new CbComNodeCommit(blockAndSubId.getBlockId(), blockAndSubId.getSubId(),
                            new NidVer(versionsRecord.toString())));*/
    }

    public void removedOrUpdated(ClassId classId, ORID versionsRecord, boolean removed) {
        System.out.println(
                MessageFormat.format("removedOrUpdated: {0}, {1}", classId, versionsRecord));
    }
}
