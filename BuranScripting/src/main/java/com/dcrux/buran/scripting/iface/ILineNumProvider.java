package com.dcrux.buran.scripting.iface;

import java.io.Serializable;

/**
 * Buran.
 *
 * @author: ${USER} Date: 07.07.13 Time: 23:55
 */
public interface ILineNumProvider extends Serializable {
    LineNum getLineNum();
}
