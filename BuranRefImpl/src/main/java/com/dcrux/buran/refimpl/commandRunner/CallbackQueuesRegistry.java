package com.dcrux.buran.refimpl.commandRunner;

import com.dcrux.buran.callbacksBase.ICallbackCommand;
import com.dcrux.buran.common.UserId;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

/**
 * Buran.
 *
 * @author: ${USER} Date: 17.07.13 Time: 03:06
 */
public class CallbackQueuesRegistry {

    private final Map<UserId, Queue<ICallbackCommand>> commandQueuesUnsync = new HashMap<>();
    private final Map<UserId, Queue<ICallbackCommand>> commandQueues =
            Collections.synchronizedMap(this.commandQueuesUnsync);

    public void register(UserId receiver, Queue<ICallbackCommand> callbackCommandQueue) {
        this.commandQueues.put(receiver, callbackCommandQueue);
    }

    public boolean emit(UserId receiver, ICallbackCommand command) {
        final Queue<ICallbackCommand> commandQueue = this.commandQueues.get(receiver);
        if (commandQueue == null) {
            return false;
        }
        return commandQueue.offer(command);
    }
}
