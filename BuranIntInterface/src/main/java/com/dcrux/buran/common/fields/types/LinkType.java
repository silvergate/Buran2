package com.dcrux.buran.common.fields.types;

import com.dcrux.buran.common.INidCommon;
import com.dcrux.buran.common.fields.typeDef.ITypeDef;
import com.dcrux.buran.common.fields.typeDef.TypeMaxMemRequirement;

/**
 * Buran.
 *
 * @author: ${USER} Date: 29.06.13 Time: 16:22
 */
public class LinkType implements ITypeDef {

    //TODO: Constraints wie
    // - classHash
    // - internal only
    // - versioned, unversioned or both
    // und solche sachen noch rein.

    public LinkType() {
    }

    public static LinkType c() {
        return new LinkType();
    }

    @Override
    public boolean isValid(Object javaData) {
        return true;
    }

    @Override
    public TypeMaxMemRequirement getMaxMemoryRequirement() {
        return new TypeMaxMemRequirement(INidCommon.MAX_LEN * 4 + 8 + 1 + 8);
    }
}
