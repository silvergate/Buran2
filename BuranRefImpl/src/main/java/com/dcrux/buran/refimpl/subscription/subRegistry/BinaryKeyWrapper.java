package com.dcrux.buran.refimpl.subscription.subRegistry;

import com.dcrux.buran.common.indexing.KeyComparator;

import java.util.Arrays;

/**
 * Buran.
 *
 * @author: ${USER} Date: 17.07.13 Time: 02:06
 */
@Deprecated
public class BinaryKeyWrapper implements Comparable<BinaryKeyWrapper> {
    private final byte[] key;

    public BinaryKeyWrapper(byte[] key) {
        this.key = key;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BinaryKeyWrapper that = (BinaryKeyWrapper) o;

        if (!Arrays.equals(key, that.key)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(key);
    }

    @Override
    public int compareTo(BinaryKeyWrapper o) {
        return KeyComparator.compareTo(this.key, o.key);
    }
}
