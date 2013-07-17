package com.dcrux.buran.callbackCommands;

import com.dcrux.buran.callbacksBase.ICallbackCommand;
import com.dcrux.buran.common.NidVer;
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
    private NidVer nidVer;

    public CbComNodeCommit(SubBlockId subBlockId, SubId subId, NidVer nidVer) {
        this.subBlockId = subBlockId;
        this.subId = subId;
        this.nidVer = nidVer;
    }

    private CbComNodeCommit() {
    }

    public SubBlockId getSubBlockId() {
        return subBlockId;
    }

    public SubId getSubId() {
        return subId;
    }

    public NidVer getNidVer() {
        return nidVer;
    }

    @Override
    public String toString() {
        return "CbComNodeCommit{" +
                "subBlockId=" + subBlockId +
                ", subId=" + subId +
                ", nidVer=" + nidVer +
                '}';
    }
}
