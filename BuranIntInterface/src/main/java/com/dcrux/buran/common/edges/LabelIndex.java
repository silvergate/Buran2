package com.dcrux.buran.common.edges;

import java.io.Serializable;

/**
 * Buran.
 *
 * @author: ${USER} Date: 01.07.13 Time: 01:41
 */
public final class LabelIndex implements Serializable {

    public static final LabelIndex MIN = new LabelIndex(Long.MIN_VALUE);
    public static final LabelIndex MAX = new LabelIndex(Long.MAX_VALUE);

    private long index;

    public LabelIndex(long index) {
        this.index = index;
    }

    private LabelIndex() {
    }

    public static LabelIndex c(long index) {
        return new LabelIndex(index);
    }

    public long getIndex() {
        return index;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LabelIndex that = (LabelIndex) o;

        if (index != that.index) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (int) index;
    }

    @Override
    public String toString() {
        return "LabelIndex{" +
                "index=" + index +
                '}';
    }
}
