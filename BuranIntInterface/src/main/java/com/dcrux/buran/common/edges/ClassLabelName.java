package com.dcrux.buran.common.edges;

/**
 * Buran.
 *
 * @author: ${USER} Date: 01.07.13 Time: 02:03
 */
public class ClassLabelName implements ILabelName {
    private final short index;

    public ClassLabelName(short index) {
        this.index = index;
    }

    public static ClassLabelName c(int index) {
        return new ClassLabelName((short) index);
    }

    public short getIndex() {
        return index;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClassLabelName that = (ClassLabelName) o;

        if (index != that.index) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (int) index;
    }

    @Override
    public String toString() {
        return "ClassLabelName{" +
                "index=" + index +
                '}';
    }
}
