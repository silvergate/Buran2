package com.dcrux.buran.common.fields.types;

import com.dcrux.buran.common.fields.typeDef.ITypeDef;
import com.dcrux.buran.common.fields.typeDef.TypeMaxMemRequirement;

/**
 * Buran.
 *
 * @author: ${USER} Date: 29.06.13 Time: 16:22
 */
public class IntegerType implements ITypeDef {

    public static enum NumOfBits {
        int8((short) 1),
        int16((short) 2),
        int32((short) 4),
        int64((short) 8);
        short bytes;

        private NumOfBits(short bytes) {
            this.bytes = bytes;
        }
    }

    private final long minValue;
    private final long maxValue;

    public IntegerType(long minValue, long maxValue) {
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    public static IntegerType cInt8Range() {
        return new IntegerType(Byte.MIN_VALUE, Byte.MAX_VALUE);
    }

    public static IntegerType cInt16Range() {
        return new IntegerType(Short.MIN_VALUE, Short.MAX_VALUE);
    }

    public static IntegerType cInt32Range() {
        return new IntegerType(Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    public static IntegerType cInt64Range() {
        return new IntegerType(Long.MIN_VALUE, Long.MAX_VALUE);
    }

    public long getMinValue() {
        return minValue;
    }

    public long getMaxValue() {
        return maxValue;
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

    public boolean fitsInRange(Number num) {
        final long value = num.longValue();
        return (value >= this.minValue) && (value <= this.maxValue);
    }

    @Override
    public boolean isValid(Object javaData) {
        if (!(javaData instanceof Number)) {
            return false;
        }
        return fitsInRange((Number) javaData);
    }

    @Override
    public TypeMaxMemRequirement getMaxMemoryRequirement() {
        return new TypeMaxMemRequirement(getRequiredBits().bytes);
    }
}
