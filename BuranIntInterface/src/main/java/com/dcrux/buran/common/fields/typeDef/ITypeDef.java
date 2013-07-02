package com.dcrux.buran.common.fields.typeDef;

import java.io.Serializable;

/**
 * Buran.
 *
 * @author: ${USER} Date: 29.06.13 Time: 16:23
 */
public interface ITypeDef extends Serializable {
    boolean isValid(Object javaData);

    TypeMaxMemRequirement getMaxMemoryRequirement();
}
