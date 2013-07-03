package com.dcrux.buran.commandBase;

import com.dcrux.buran.common.UserId;

import java.io.Serializable;

/**
 * Buran.
 *
 * @author: ${USER} Date: 02.07.13 Time: 14:19
 */
public interface ICommandRunner {
    <TRetval extends Serializable> TRetval sync(UserId receiver, UserId sender,
            ICommand<TRetval> command)
            throws UncheckedException, UnknownCommandException, WrappedExpectableException;
}
