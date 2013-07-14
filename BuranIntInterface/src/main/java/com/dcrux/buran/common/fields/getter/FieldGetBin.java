package com.dcrux.buran.common.fields.getter;

/**
 * Buran.
 *
 * @author: ${USER} Date: 30.06.13 Time: 13:10
 */
public class FieldGetBin implements IUnfieldedDataGetter<byte[]> {
    private final long skip;
    private final int number;

    public FieldGetBin(long skip, int number) {
        this.skip = skip;
        this.number = number;
    }

    public long getSkip() {
        return skip;
    }

    public int getNumber() {
        return number;
    }
}
