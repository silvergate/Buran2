package com.dcrux.buran.utils;

import java.io.Serializable;
import java.util.List;

/**
 * Buran.
 *
 * @author: ${USER} Date: 13.08.13 Time: 14:15
 */
public interface ISerList<TType extends Serializable> extends List<TType>, Serializable {
}
