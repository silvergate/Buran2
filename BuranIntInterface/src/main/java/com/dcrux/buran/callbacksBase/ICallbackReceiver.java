package com.dcrux.buran.callbacksBase;

import com.dcrux.buran.common.UserId;

import java.util.Queue;

/**
 * Buran.
 *
 * @author: ${USER} Date: 17.07.13 Time: 03:00
 */
public interface ICallbackReceiver {
    void register(UserId receiver, Queue<ICallbackCommand> callbackCommandQueue);
}
