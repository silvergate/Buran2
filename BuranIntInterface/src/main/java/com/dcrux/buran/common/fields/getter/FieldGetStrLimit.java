package com.dcrux.buran.common.fields.getter;

/**
 * Buran.
 *
 * @author: ${USER} Date: 30.06.13 Time: 13:10
 */
public class FieldGetStrLimit extends FieldGetPrim<String> implements IUnfieldedDataGetter<String> {

    private int maxNumOfChars;

    public FieldGetStrLimit(int maxNumOfChars) {
        if (maxNumOfChars < 1) {
            throw new IllegalArgumentException("(maxNumOfChars<1)");
        }
        this.maxNumOfChars = maxNumOfChars;
    }

    public static FieldGetStrLimit limit(int maxNumOfChars) {
        return new FieldGetStrLimit(maxNumOfChars);
    }

    public int getMaxNumOfChars() {
        return maxNumOfChars;
    }

    private FieldGetStrLimit() {
    }
}
