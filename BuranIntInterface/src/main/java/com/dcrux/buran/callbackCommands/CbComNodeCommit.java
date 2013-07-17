package com.dcrux.buran.callbackCommands;

import com.dcrux.buran.callbacksBase.ICallbackCommand;
import com.dcrux.buran.common.subscription.SubBlockId;
import com.dcrux.buran.common.subscription.SubId;

/**
 * Buran.
 *
 * @author: ${USER} Date: 17.07.13 Time: 03:44
 */
public class CbComNodeCommit extends ICallbackCommand {
    private SubBlockId subBlockId;
    private SubId subId;
    private String orid;

    public CbComNodeCommit(SubBlockId subBlockId, SubId subId, String orid) {
        this.subBlockId = subBlockId;
        this.subId = subId;
        this.orid = orid;
    }

    private CbComNodeCommit() {
    }

    public SubBlockId getSubBlockId() {
        return subBlockId;
    }

    public SubId getSubId() {
        return subId;
    }

    public String getOrid() {
        return orid;
    }

    @Override
    public String toString() {
        return "CbComNodeCommit{" +
                "subBlockId=" + subBlockId +
                ", subId=" + subId +
                ", orid='" + orid + '\'' +
                '}';
    }
}
