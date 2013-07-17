package com.dcrux.buran.common.classes;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Buran.
 *
 * @author: ${USER} Date: 02.07.13 Time: 20:06
 */
public final class ClassHashId implements Serializable {
    private byte[] hash;

    public static final int HASH_LEN = 32;

    public ClassHashId(byte[] hash) {
        if (hash.length != HASH_LEN) {
            throw new IllegalArgumentException("hash.length!=HASH_LEN");
        }
        this.hash = hash;
    }

    private ClassHashId() {
    }

    /*public String asString() {
        return BaseEncoding.base64().encode(this.hash);
    } */

    @Override
    public String toString() {
        return "ClassHashId{" + Arrays.toString(this.hash) + "}";
    }

    /*public static ClassHashId fromString(final String hashAsString) {
        final byte[] data = BaseEncoding.base64().decode(hashAsString);
        return new ClassHashId(data);
    } */

    public byte[] getHash() {
        return hash;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClassHashId that = (ClassHashId) o;

        if (!Arrays.equals(hash, that.hash)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(hash);
    }
}
