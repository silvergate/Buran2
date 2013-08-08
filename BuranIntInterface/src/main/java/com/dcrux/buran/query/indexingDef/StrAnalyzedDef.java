package com.dcrux.buran.query.indexingDef;

import com.dcrux.buran.common.fields.getter.IUnfieldedDataGetter;
import com.dcrux.buran.common.fields.types.StringType;

import java.util.EnumSet;

/**
 * Buran.
 *
 * @author: ${USER} Date: 06.08.13 Time: 11:37
 */
public class StrAnalyzedDef implements IIndexingDef<String> {

    public static enum SupportedTypes {
        phrase,
        andTerms,
        orTerms
    }

    public static final int MAX_LEN_CHARS = StringType.MAXLEN_LIMIT;

    private boolean requiredForIndex;
    private int numberOfChars;
    private IUnfieldedDataGetter<String> dataGetter;
    private EnumSet<SupportedTypes> supportedTypes;

    public StrAnalyzedDef(boolean requiredForIndex, int numberOfChars,
            IUnfieldedDataGetter<String> dataGetter) {
        this.requiredForIndex = requiredForIndex;
        this.numberOfChars = numberOfChars;
        this.dataGetter = dataGetter;
        this.supportedTypes = EnumSet.allOf(SupportedTypes.class);
    }

    public int getNumberOfChars() {
        return numberOfChars;
    }

    public EnumSet<SupportedTypes> getSupportedTypes() {
        return supportedTypes;
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
