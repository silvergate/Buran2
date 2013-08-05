package com.dcrux.buran.utils;

import java.io.Serializable;

/**
 * Buran.
 *
 * @author: ${USER} Date: 05.08.13 Time: 00:36
 */
public interface IAltType<TThisType extends Serializable> extends Serializable {
    Class<? extends TThisType> getType();

    <TType extends TThisType> TType get(Class<TType> type);

    boolean is(Class<? extends TThisType> type);
}
