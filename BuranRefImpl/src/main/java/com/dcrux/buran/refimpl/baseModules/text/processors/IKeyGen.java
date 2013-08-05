package com.dcrux.buran.refimpl.baseModules.text.processors;

import com.sun.istack.internal.Nullable;

/**
 * Buran.
 *
 * @author: ${USER} Date: 05.08.13 Time: 22:34
 */
public interface IKeyGen {
    @Nullable
    KeyRange generateKey(String query);
}
