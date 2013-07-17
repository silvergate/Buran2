package com.dcrux.buran.common;

import java.io.Serializable;

/**
 * Buran.
 *
 * @author: ${USER} Date: 01.07.13 Time: 10:01
 */
public interface INidCommon extends Serializable {
    String getAsString();

    static final int MAX_LEN = 40;
    static final int MIN_LEN = 1;
}
