package com.dcrux.buran.common.fields.types;

import com.dcrux.buran.common.fields.typeDef.ITypeDef;
import com.dcrux.buran.common.fields.typeDef.TypeMaxMemRequirement;

/**
 * Buran.
 *
 * @author: ${USER} Date: 29.06.13 Time: 16:22
 */
public class StringType implements ITypeDef {

    private final int minLen;
    private final int maxLen;

    public StringType(int minLen, int maxLen) {
        this.minLen = minLen;
        this.maxLen = maxLen;
    }

    public int getMinLen() {
        return minLen;
    }

    public int getMaxLen() {
        return maxLen;
    }

    @Override
    public boolean isValid(Object javaData) {
        //TODO: Check max and min len
        return javaData instanceof String;
    }

    @Override
    public TypeMaxMemRequirement getMaxMemoryRequirement() {
        return new TypeMaxMemRequirement(this.maxLen * 4);
    }
}
