package com.dcrux.buran.common.fields.setter;

import com.dcrux.buran.common.fields.typeDef.ITypeDef;

import java.io.Serializable;

/**
 * Buran.
 *
 * @author: ${USER} Date: 02.07.13 Time: 17:56
 */
public interface IUnfieldedDataSetter extends Serializable {
    @Deprecated
    boolean canApplyTo(ITypeDef dataType);
}
