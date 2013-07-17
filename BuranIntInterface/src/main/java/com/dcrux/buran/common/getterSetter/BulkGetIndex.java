package com.dcrux.buran.common.getterSetter;

import java.io.Serializable;

/**
 * Buran.
 *
 * @author: ${USER} Date: 03.07.13 Time: 02:20
 */
public class BulkGetIndex<TRetVal> implements Serializable {
    private int index;

    public BulkGetIndex(int index) {
        this.index = index;
    }

    private BulkGetIndex() {
    }

    public int getIndex() {
        return index;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BulkGetIndex that = (BulkGetIndex) o;

        if (index != that.index) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return index;
    }

    @Override
    public String toString() {
        return "BulkGetIndex{" +
                "index=" + index +
                '}';
    }
}
