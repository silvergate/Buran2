package com.dcrux.buran.fields.types;

import java.io.Serializable;

/**
 * Buran.
 *
 * @author: ${USER} Date: 29.06.13 Time: 16:23
 */
public interface IType extends Serializable {
    boolean isValid(Object javaData);
}
