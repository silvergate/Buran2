package com.dcrux.buran.commandBase;

import java.io.Serializable;
import java.util.Set;

/**
 * Buran.
 *
 * @author: ${USER} Date: 02.07.13 Time: 14:12
 */
public interface ICommand<TRetval extends Serializable> extends Serializable {
    Set<Class<? extends Exception>> getExpectableExceptions();
}
