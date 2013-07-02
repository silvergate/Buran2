package com.dcrux.buran.refimpl.baseModules.auth;

import com.dcrux.buran.common.UserId;
import com.dcrux.buran.refimpl.baseModules.BaseModule;
import com.dcrux.buran.refimpl.baseModules.common.Module;

/**
 * Buran.
 *
 * @author: ${USER} Date: 02.07.13 Time: 15:24
 */
public class AuthModule extends Module<BaseModule> {
    public AuthModule(BaseModule baseModule) {
        super(baseModule);
    }

    private UserId sender;
    private UserId receiver;

    public UserId getSender() {
        return sender;
    }

    public UserId getReceiver() {
        return receiver;
    }

    public void setSender(UserId sender) {
        this.sender = sender;
    }

    public void setReceiver(UserId receiver) {
        this.receiver = receiver;
    }
}
