package com.dcrux.buran.common.labels;

/**
 * Buran.
 *
 * @author: ${USER} Date: 01.07.13 Time: 01:41
 */
public class LabelIndex {
    private final long index;

    public LabelIndex(long index) {
        this.index = index;
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