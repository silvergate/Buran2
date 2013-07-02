package com.dcrux.buran.fields.setter;

import com.dcrux.buran.fields.types.IType;

import java.io.Serializable;

/**
 * Buran.
 *
 * @author: ${USER} Date: 29.06.13 Time: 16:24
 */
public interface IDataSetter extends Serializable {
    boolean canApplyTo(IType dataType);
}
