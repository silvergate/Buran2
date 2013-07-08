package com.dcrux.buran.refimpl.baseModules.index.eval;

import java.util.Arrays;

/**
 * Buran.
 *
 * @author: ${USER} Date: 08.07.13 Time: 23:58
 */
public class EvaluatedMap {
    private final byte[] key;
    private final Object value;

    public EvaluatedMap(byte[] key, Object value) {
        this.key = key;
        this.value = value;
    }

    public byte[] getKey() {
        return key;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "EvaluatedMap{" +
                "key=" + Arrays.toString(key) +
                ", value=" + value +
                '}';
    }
}
