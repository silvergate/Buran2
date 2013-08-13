package com.dcrux.buran.common.query.indexingDef;

import com.dcrux.buran.common.fields.getter.IUnfieldedDataGetter;

/**
 * Buran.
 *
 * @author: ${USER} Date: 06.08.13 Time: 11:37
 */
public class StrExactIndexingDef implements IIndexingDef<String> {

    public static final int MAX_LEN_CHARS = 128;

    private boolean requiredForIndex;
    private int numberOfChars;
    private IUnfieldedDataGetter<String> dataGetter;

    public StrExactIndexingDef(boolean requiredForIndex, int numberOfChars,
            IUnfieldedDataGetter<String> dataGetter) {
        this.requiredForIndex = requiredForIndex;
        if (numberOfChars > MAX_LEN_CHARS) {
            throw new IllegalArgumentException("numberOfChars>MAX_LEN_CHARS");
        }
        this.numberOfChars = numberOfChars;
        this.dataGetter = dataGetter;
    }

    private StrExactIndexingDef() {
    }

    public int getNumberOfChars() {
        return numberOfChars;
    }

    @Override
    public IUnfieldedDataGetter<String> getDataGetter() {
        return this.dataGetter;
    }

    @Override
    public boolean requiredForIndex() {
        return this.requiredForIndex;
    }
}
