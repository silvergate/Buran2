package com.dcrux.buran.common;

/**
 * Buran.
 *
 * @author: ${USER} Date: 29.06.13 Time: 15:35
 */
public final class NidConstructor implements INid {

    private final String value;

    public NidConstructor(String value) {
        this.value = value;
    }

    @Override
    public String getAsString() {
        return this.value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NidConstructor that = (NidConstructor) o;

        if (!value.equals(that.value)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
