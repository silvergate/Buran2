package com.dcrux.buran.refimpl.baseModules.text.processors.impl.enUK;

import com.dcrux.buran.refimpl.baseModules.text.processors.IFilter;

/**
 * Buran.
 *
 * @author: ${USER} Date: 05.08.13 Time: 19:54
 */
public class Filter implements IFilter {
    @Override
    public boolean use(String token) {
        return true;
    }
}
