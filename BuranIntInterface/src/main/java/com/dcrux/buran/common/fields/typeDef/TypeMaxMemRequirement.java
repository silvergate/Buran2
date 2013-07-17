package com.dcrux.buran.common.fields.typeDef;

import java.io.Serializable;

/**
 * Buran.
 *
 * @author: ${USER} Date: 02.07.13 Time: 19:40
 */
public final class TypeMaxMemRequirement implements Serializable {
    private long bytes;

    public TypeMaxMemRequirement(long bytes) {
        this.bytes = bytes;
    }

    private TypeMaxMemRequirement() {
    }

    public long getBytes() {
        return bytes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TypeMaxMemRequirement that = (TypeMaxMemRequirement) o;

        if (bytes != that.bytes) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (int) (bytes ^ (bytes >>> 32));
    }

    @Override
    public String toString() {
        return "TypeMaxMemRequirement{" +
                "bytes=" + bytes +
                '}';
    }
}
