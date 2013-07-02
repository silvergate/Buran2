package com.dcrux.buran.refimpl.commandDispatchBase;

import com.dcrux.buran.commandBase.ICommand;

import java.io.Serializable;

/**
 * Buran.
 *
 * @author: ${USER} Date: 02.07.13 Time: 14:58
 */
public interface ICommandInvoker {
    <TRunParam, TRetval extends Serializable, TCommand extends ICommand<TRetval>> TRetval invoke(
            TCommand command, ICommandImpl<TRunParam, TRetval, TCommand> impl,
            ICommandRunParamProvider<TRunParam> commandRunParamProvider) throws Exception;
}
