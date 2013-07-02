package com.dcrux.buran.labels;

/**
 * Buran.
 *
 * @author: ${USER} Date: 01.07.13 Time: 02:03
 */
public class ExtLabelName implements ILabelName {
    private final long low;
    private final long hi;

    public ExtLabelName(long low, long hi) {
        this.low = low;
        this.hi = hi;
    }

    public long getLow() {
        return low;
    }

    public long getHi() {
        return hi;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ExtLabelName that = (ExtLabelName) o;

        if (hi != that.hi) return false;
        if (low != that.low) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (low ^ (low >>> 32));
        result = 31 * result + (int) (hi ^ (hi >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "ExtLabelName{" +
                "low=" + low +
                ", hi=" + hi +
                '}';
    }
}
