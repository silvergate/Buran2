package com.dcrux.buran;

/**
 * Buran.
 *
 * @author: ${USER} Date: 29.06.13 Time: 15:35
 */
public final class IncNidConstructor implements IIncNid {

    private final String value;

    public IncNidConstructor(String value) {
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

        IncNidConstructor that = (IncNidConstructor) o;

        if (!value.equals(that.value)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
