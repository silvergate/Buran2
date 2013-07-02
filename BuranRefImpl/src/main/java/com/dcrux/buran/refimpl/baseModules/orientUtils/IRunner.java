package com.dcrux.buran.refimpl.baseModules.orientUtils;

/**
 * Buran.
 *
 * @author: ${USER} Date: 29.06.13 Time: 16:58
 */
public interface IRunner {
    void run(ITransaction transaction) throws Throwable;

    <T extends Object> T run(ITransRet<T> transaction) throws Throwable;

    IRunner reuse();
}
