package com.dcrux.buran.refimpl.commandDispatchBase;

import com.dcrux.buran.commandBase.ICommand;

import java.io.Serializable;

/**
 * Buran.
 *
 * @author: ${USER} Date: 02.07.13 Time: 14:31
 */
public interface ICommandImpl<TRunParam extends Object, TRetval extends Serializable,
        TCommand extends ICommand<TRetval>> {
    Class<?> getCommandClass();

    Class<TRunParam> getRunParamClass();

    TRetval run(TCommand command, TRunParam runParam) throws Exception;
}
