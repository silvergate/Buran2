package com.dcrux.buran.utils;

import java.io.Serializable;

/**
 * Buran.
 *
 * @author: ${USER} Date: 05.08.13 Time: 00:50
 */
public abstract class WrappedAltType<TThisType extends Serializable>
        implements IAltType<TThisType> {

    private Serializable data;

    protected WrappedAltType(Serializable data) {
        if (data == null) {
            throw new IllegalArgumentException("data==null");
        }
        this.data = data;
    }

    @Override
    public Class<? extends TThisType> getType() {
        return (Class<? extends TThisType>) data.getClass();
    }

    @Override
    public final <TType extends TThisType> TType get(Class<TType> type) {
        return (TType) this.data;
    }

    @Override
    public boolean is(Class<? extends TThisType> type) {
        return (this.data.getClass().equals(type));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WrappedAltType that = (WrappedAltType) o;

        if (data != null ? !data.equals(that.data) : that.data != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return data != null ? data.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "WrappedAltType{" +
                "data=" + data +
                '}';
    }
}
