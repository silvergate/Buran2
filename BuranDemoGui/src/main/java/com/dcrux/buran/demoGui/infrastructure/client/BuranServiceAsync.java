package com.dcrux.buran.demoGui.infrastructure.client;

import com.dcrux.buran.commandBase.ICommand;
import com.dcrux.buran.common.UserId;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.io.Serializable;

/**
 * Buran.
 *
 * @author: ${USER} Date: 14.07.13 Time: 23:21
 */
public interface BuranServiceAsync {
    <TRetVal extends Serializable> void run(UserId receiver, ICommand<TRetVal> command,
            AsyncCallback<TRetVal> async);

    void serTest(Serializable obj, AsyncCallback<Void> async);
}
