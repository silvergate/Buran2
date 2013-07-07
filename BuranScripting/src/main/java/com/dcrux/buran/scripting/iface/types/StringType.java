package com.dcrux.buran.scripting.iface.types;

import com.dcrux.buran.scripting.iface.IType;

/**
 * Buran.
 *
 * @author: ${USER} Date: 05.07.13 Time: 10:55
 */
public class StringType implements IType<String> {
    private final int minLen;
    private final int maxLen;

    public StringType(int minLen, int maxLen) {
        this.minLen = minLen;
        this.maxLen = maxLen;
    }

    public int getMinLen() {
        return minLen;
    }

    public int getMaxLen() {
        return maxLen;
    }

    @Override
    public IType<String> combineWith(IType<?> other) {
        final StringType otherCast = (StringType) other;
        return new StringType(Math.min(getMinLen(), otherCast.getMinLen()),
                Math.max(getMaxLen(), otherCast.getMaxLen()));
    }

    @Override
    public int getMemoryMaxMemoryRequirement() {
        return this.maxLen * 4;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StringType that = (StringType) o;

        if (maxLen != that.maxLen) return false;
        if (minLen != that.minLen) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = minLen;
        result = 31 * result + maxLen;
        return result;
    }

    @Override
    public String toString() {
        return "StringType{" +
                "minLen=" + minLen +
                ", maxLen=" + maxLen +
                '}';
    }
}
