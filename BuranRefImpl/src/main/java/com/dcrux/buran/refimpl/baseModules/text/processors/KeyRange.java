package com.dcrux.buran.refimpl.baseModules.text.processors;

import java.util.Arrays;

/**
 * Buran.
 *
 * @author: ${USER} Date: 05.08.13 Time: 22:34
 */
public class KeyRange {
    private final byte[] startKey;
    private final byte[] endKey;

    public KeyRange(byte[] startKey, byte[] endKey) {
        this.startKey = startKey;
        this.endKey = endKey;
    }

    public byte[] getStartKey() {
        return startKey;
    }

    public byte[] getEndKey() {
        return endKey;
    }

    @Override
    public String toString() {
        return "KeyRange{" +
                "startKey=" + Arrays.toString(startKey) +
                ", endKey=" + Arrays.toString(endKey) +
                '}';
    }
}
