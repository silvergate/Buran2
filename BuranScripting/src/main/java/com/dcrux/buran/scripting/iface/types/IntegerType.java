package com.dcrux.buran.scripting.iface.types;

import com.dcrux.buran.scripting.iface.IType;

import java.io.Serializable;

/**
 * Buran.
 *
 * @author: ${USER} Date: 04.07.13 Time: 17:36
 */
public final class IntegerType implements IType<Number> {

    private IntegerType() {
    }

    public static enum NumOfBits implements Serializable {
        int8((short) 1),
        int16((short) 2),
        int32((short) 4),
        int64((short) 8);
        short bytes;

        private NumOfBits(short bytes) {
            this.bytes = bytes;
        }

        public short getBytes() {
            return bytes;
        }
    }

    public NumOfBits getRequiredBits() {
        if ((this.minValue >= Byte.MIN_VALUE) && (this.maxValue <= Byte.MAX_VALUE)) {
            return NumOfBits.int8;
        }
        if ((this.minValue >= Short.MIN_VALUE) && (this.maxValue <= Short.MAX_VALUE)) {
            return NumOfBits.int16;
        }
        if ((this.minValue >= Integer.MIN_VALUE) && (this.maxValue <= Integer.MAX_VALUE)) {
            return NumOfBits.int32;
        }
        return NumOfBits.int64;
    }

    private long minValue;
    private long maxValue;

    public IntegerType(long minValue, long maxValue) {
        if (maxValue < minValue) {
            throw new IllegalArgumentException("maxValue<minValue");
        }
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    public long getMinValue() {
        return minValue;
    }

    public long getMaxValue() {
        return maxValue;
    }

    @Override
    public String toString() {
        return "IntegerType{" +
                "minValue=" + minValue +
                ", maxValue=" + maxValue +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IntegerType that = (IntegerType) o;

        if (maxValue != that.maxValue) return false;
        if (minValue != that.minValue) return false;

        return true;
    }

    public boolean intersect(IntegerType other) {
        if (isStrictlyGreaterThan(other)) return false;
        if (isStrictlyLesserThan(other)) return false;
        return true;
    }

    public boolean isStrictlyGreaterThan(IntegerType other) {
        return (getMinValue() > other.getMaxValue());
    }

    public boolean isStrictlyLesserThan(IntegerType other) {
        return getMaxValue() < other.getMinValue();
    }

    @Override
    public int hashCode() {
        int result = (int) (minValue ^ (minValue >>> 32));
        result = 31 * result + (int) (maxValue ^ (maxValue >>> 32));
        return result;
    }

    @Override
    public IType<Number> combineWith(IType<?> other) {
        final IntegerType otherCast = (IntegerType) other;
        return new IntegerType(Math.min(getMinValue(), otherCast.getMinValue()),
                Math.max(getMaxValue(), otherCast.getMaxValue()));
    }

    @Override
    public int getMemoryMaxMemoryRequirement() {
        return 8;
    }
}
