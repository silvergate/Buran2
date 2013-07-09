package com.dcrux.buran.refimpl.baseModules.index.orientSerializer;

import com.dcrux.buran.indexing.KeyComparator;

/**
 * Buran.
 *
 * @author: ${USER} Date: 09.07.13 Time: 17:08
 */
//TODO: Noch eigener serializer schreiben
public class ComparableBinary implements Comparable<ComparableBinary> {
    private byte[] value;

    public ComparableBinary(byte[] buffer) {
        value = buffer;
    }

    public int compareTo(ComparableBinary o) {
        return KeyComparator.compareTo(this.value, o.value);
    }

    public byte[] toByteArray() {
        return value;
    }
}
