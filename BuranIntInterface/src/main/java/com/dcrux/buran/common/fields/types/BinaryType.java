package com.dcrux.buran.common.fields.types;

import com.dcrux.buran.common.fields.typeDef.ITypeDef;
import com.dcrux.buran.common.fields.typeDef.TypeMaxMemRequirement;

/**
 * Buran.
 *
 * @author: ${USER} Date: 29.06.13 Time: 16:22
 */
public class BinaryType implements ITypeDef {

    public static final long MINLEN_LIMIT = 0;
    public static final long MAXLEN_LIMIT = 1099511627776l;

    private final long minLen;
    private final long maxLen;

    public BinaryType(long minLen, long maxLen) {
        if (minLen < MINLEN_LIMIT) {
            throw new IllegalArgumentException("(minLen<MINLEN_LIMIT)");
        }
        if (maxLen > MAXLEN_LIMIT) {
            throw new IllegalArgumentException("(maxLen>MAXLEN_LIMIT)");
        }
        this.minLen = minLen;
        this.maxLen = maxLen;
    }

    public static BinaryType c(long maxLen) {
        return new BinaryType(0, maxLen);
    }

    public long getMinLen() {
        return minLen;
    }

    public long getMaxLen() {
        return maxLen;
    }

    @Override
    public boolean isValid(Object javaData) {
        if (!(javaData instanceof byte[])) {
            return false;
        }
        return true;
    }

    @Override
    public TypeMaxMemRequirement getMaxMemoryRequirement() {
        return new TypeMaxMemRequirement(this.maxLen * 4);
    }
}
