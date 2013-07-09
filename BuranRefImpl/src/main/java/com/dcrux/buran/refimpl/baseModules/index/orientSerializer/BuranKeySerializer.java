package com.dcrux.buran.refimpl.baseModules.index.orientSerializer;

import com.orientechnologies.common.directmemory.ODirectMemory;
import com.orientechnologies.common.serialization.OBinaryConverter;
import com.orientechnologies.common.serialization.OBinaryConverterFactory;
import com.orientechnologies.common.serialization.types.OBinarySerializer;
import com.orientechnologies.common.serialization.types.OBinaryTypeSerializer;
import com.orientechnologies.common.serialization.types.OIntegerSerializer;

import java.nio.ByteOrder;
import java.util.Arrays;

/**
 * Buran.
 *
 * @author: ${USER} Date: 09.07.13 Time: 17:33
 */
public class BuranKeySerializer implements OBinarySerializer<ComparableBinary> {

    private static final OBinaryConverter CONVERTER = OBinaryConverterFactory.getConverter();
    public static final OBinaryTypeSerializer INSTANCE = new OBinaryTypeSerializer();
    public static final byte ID = 119;
    public static final int LENGTH = 32;

    public int getObjectSize(final int length) {
        return length;
    }

    public int getObjectSize(final ComparableBinary object) {
        return object.toByteArray().length;
    }

    public void serialize(final ComparableBinary object, final byte[] stream,
            final int startPosition) {
        final byte[] buffer = object.toByteArray();
        System.arraycopy(buffer, 0, stream, startPosition, buffer.length);
    }

    public ComparableBinary deserialize(final byte[] stream, final int startPosition) {
        final byte[] buffer = Arrays.copyOfRange(stream, startPosition, startPosition + LENGTH);
        return new ComparableBinary(buffer);
    }

    public int getObjectSize(byte[] stream, int startPosition) {
        return LENGTH;
    }

    public byte getId() {
        return ID;
    }

    @Override
    public boolean isFixedLength() {
        return false;
    }

    @Override
    public int getFixedLength() {
        return 0;
    }

    @Override
    public void serializeNative(ComparableBinary object, byte[] stream, int startPosition) {
        int len = object.toByteArray().length;
        CONVERTER.putInt(stream, startPosition, len, ByteOrder.nativeOrder());
        System.arraycopy(object.toByteArray(), 0, stream,
                startPosition + OIntegerSerializer.INT_SIZE, len);
    }

    @Override
    public ComparableBinary deserializeNative(byte[] stream, int startPosition) {
        int len = CONVERTER.getInt(stream, startPosition, ByteOrder.nativeOrder());
        return new ComparableBinary(
                Arrays.copyOfRange(stream, startPosition + OIntegerSerializer.INT_SIZE,
                        startPosition + OIntegerSerializer.INT_SIZE + len));
    }

    @Override
    public int getObjectSizeNative(byte[] stream, int startPosition) {
        return CONVERTER.getInt(stream, startPosition, ByteOrder.nativeOrder()) +
                OIntegerSerializer.INT_SIZE;
    }

    @Override
    public void serializeInDirectMemory(ComparableBinary object, ODirectMemory memory,
            long pointer) {
        int len = object.toByteArray().length;
        memory.setInt(pointer, len);
        pointer += OIntegerSerializer.INT_SIZE;

        memory.set(pointer, object.toByteArray(), 0, len);
    }

    @Override
    public ComparableBinary deserializeFromDirectMemory(ODirectMemory memory, long pointer) {
        int len = memory.getInt(pointer);
        pointer += OIntegerSerializer.INT_SIZE;

        return new ComparableBinary(memory.get(pointer, len));
    }

    @Override
    public int getObjectSizeInDirectMemory(ODirectMemory memory, long pointer) {
        return memory.getInt(pointer) + OIntegerSerializer.INT_SIZE;
    }
}