package com.dcrux.buran.refimpl.service;

/**
 * Buran.
 *
 * @author: ${USER} Date: 29.06.13 Time: 15:44
 */
public class CurrentTimestampProvider {
    public long get() {
        return System.currentTimeMillis();
    }
}
