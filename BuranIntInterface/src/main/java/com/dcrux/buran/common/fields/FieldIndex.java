package com.dcrux.buran.common.fields;

import java.io.Serializable;

/**
 * Buran.
 *
 * @author: ${USER} Date: 29.06.13 Time: 16:29
 */
public final class FieldIndex implements Serializable {
    private final short index;

    public FieldIndex(short index) {
        this.index = index;
    }

    public static FieldIndex c(int index) {
        return new FieldIndex((short) index);
    }

    public short getIndex() {
        return index;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FieldIndex that = (FieldIndex) o;

        if (index != that.index) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (int) index;
    }

    @Override
    public String toString() {
        return "FieldIndex{" +
                "index=" + index +
                '}';
    }
}
