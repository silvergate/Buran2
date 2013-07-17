package com.dcrux.buran.scripting.iface.types;

import com.dcrux.buran.scripting.iface.IType;

/**
 * Buran.
 *
 * @author: ${USER} Date: 05.07.13 Time: 10:55
 */
public class BinType implements IType<byte[]> {
    private int maxBytes;
    private int minBytes;

    public BinType(int minLen, int minBytes) {
        this.maxBytes = minLen;
        this.minBytes = minBytes;
    }

    private BinType() {
    }

    public int getMaxBytes() {
        return maxBytes;
    }

    public int getMinBytes() {
        return minBytes;
    }

    @Override
    public IType<byte[]> combineWith(IType<?> other) {
        final BinType otherCast = (BinType) other;
        return new BinType(Math.min(getMinBytes(), otherCast.getMinBytes()),
                Math.max(getMaxBytes(), otherCast.getMaxBytes()));
    }

    @Override
    public int getMemoryMaxMemoryRequirement() {
        return this.maxBytes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BinType that = (BinType) o;

        if (minBytes != that.minBytes) return false;
        if (maxBytes != that.maxBytes) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = maxBytes;
        result = 31 * result + minBytes;
        return result;
    }

    @Override
    public String toString() {
        return "BinType{" +
                "maxBytes=" + maxBytes +
                ", minBytes=" + minBytes +
                '}';
    }
}
