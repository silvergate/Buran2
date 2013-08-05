package com.dcrux.buran.utils;

import java.io.Serializable;

/**
 * Buran.
 *
 * @author: ${USER} Date: 05.08.13 Time: 00:32
 */
public class AltType<TThisType extends Serializable> implements IAltType<TThisType> {

    @Override
    public Class<? extends TThisType> getType() {
        return (Class<? extends TThisType>) this.getClass();
    }

    @Override
    public final <TType extends TThisType> TType get(Class<TType> type) {
        return (TType) this;
    }

    @Override
    public boolean is(Class<? extends TThisType> type) {
        return (this.getClass().equals(type));
    }
}
