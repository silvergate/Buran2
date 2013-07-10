package com.dcrux.buran.common.indexing.keyGen;

import java.nio.ByteBuffer;

/**
 * Buran.
 *
 * @author: ${USER} Date: 09.07.13 Time: 21:13
 */
public class NumberKeyGen implements ISingleIndexKeyGen {

    public static enum Type {
        int8(1),
        int16(2),
        int32(4),
        int64(8);
        short length;

        private Type(int length) {
            this.length = (short) length;
        }

        public short getLength() {
            return length;
        }
    }

    private final Type type;
    private final Number number;

    public NumberKeyGen(Type type, Number number) {
        this.type = type;
        this.number = number;
    }

    public static NumberKeyGen int8(byte number) {
        return new NumberKeyGen(Type.int8, number);
    }

    public static NumberKeyGen int16(short number) {
        return new NumberKeyGen(Type.int16, number);
    }

    public static NumberKeyGen int32(int number) {
        return new NumberKeyGen(Type.int32, number);
    }

    public static NumberKeyGen int64(long number) {
        return new NumberKeyGen(Type.int64, number);
    }

    @Override
    public int getLength() {
        return this.type.getLength();
    }

    @Override
    public void generateKey(ByteBuffer byteBuffer) {
        switch (this.type) {
            case int8:
                byteBuffer.put(this.number.byteValue());
                break;
            case int16:
                byteBuffer.putShort(this.number.shortValue());
                break;
            case int32:
                byteBuffer.putInt(this.number.intValue());
                break;
            case int64:
                byteBuffer.putLong(this.number.longValue());
                break;
        }
    }
}
