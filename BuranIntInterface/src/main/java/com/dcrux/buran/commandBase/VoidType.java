package com.dcrux.buran.commandBase;

import java.io.Serializable;

/**
 * Buran.
 *
 * @author: ${USER} Date: 02.07.13 Time: 17:19
 */
public class VoidType implements Serializable {
    private VoidType() {
    }

    public static final VoidType SINGLETON = new VoidType();
}
