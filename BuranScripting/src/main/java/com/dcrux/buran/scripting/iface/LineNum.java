package com.dcrux.buran.scripting.iface;

/**
 * Buran.
 *
 * @author: ${USER} Date: 05.07.13 Time: 07:39
 */
public final class LineNum implements ILineNumProvider {
    private final int num;

    public LineNum(int num) {
        this.num = num;
    }

    public int getNum() {
        return num;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LineNum lineNum = (LineNum) o;

        if (num != lineNum.num) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return num;
    }

    @Override
    public String toString() {
        return "LineNum{" +
                "num=" + num +
                '}';
    }

    @Override
    public LineNum getLineNum() {
        return this;
    }
}
