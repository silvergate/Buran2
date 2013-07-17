package com.dcrux.buran.common.fields.types;

import com.dcrux.buran.common.fields.typeDef.ITypeDef;
import com.dcrux.buran.common.fields.typeDef.TypeMaxMemRequirement;

/**
 * Buran.
 *
 * @author: ${USER} Date: 29.06.13 Time: 16:22
 */
public class StringType implements ITypeDef {

    public static final int MINLEN_LIMIT = 0;
    public static final int MAXLEN_LIMIT = 262144;

    private int minLen;
    private int maxLen;

    public StringType(int minLen, int maxLen) {
        if (minLen < MINLEN_LIMIT) {
            throw new IllegalArgumentException("(minLen<MINLEN_LIMIT)");
        }
        if (maxLen > MAXLEN_LIMIT) {
            throw new IllegalArgumentException("(maxLen>MAXLEN_LIMIT)");
        }
        this.minLen = minLen;
        this.maxLen = maxLen;
    }

    private StringType() {
    }

    public int getMinLen() {
        return minLen;
    }

    public int getMaxLen() {
        return maxLen;
    }

    public boolean isMinMaxOk(String str) {
        final int len = str.length();
        return (len >= this.minLen) && (len <= this.maxLen);
    }

    @Override
    public boolean isValid(Object javaData) {
        if (!(javaData instanceof String)) {
            return false;
        }
        return isMinMaxOk((String) javaData);
    }

    @Override
    public TypeMaxMemRequirement getMaxMemoryRequirement() {
        return new TypeMaxMemRequirement(this.maxLen * 4);
    }
}
