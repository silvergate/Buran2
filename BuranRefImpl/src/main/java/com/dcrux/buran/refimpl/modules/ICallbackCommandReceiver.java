package com.dcrux.buran.refimpl.modules;

import com.dcrux.buran.callbacksBase.ICallbackCommand;
import com.dcrux.buran.common.UserId;

/**
 * Buran.
 *
 * @author: ${USER} Date: 17.07.13 Time: 03:15
 */
public interface ICallbackCommandReceiver {
    boolean emit(UserId receiver, ICallbackCommand command);
}
