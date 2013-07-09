package com.dcrux.buran.refimpl.baseModules.index.keyGen;

/**
 * Buran.
 *
 * @author: ${USER} Date: 09.07.13 Time: 22:56
 */
public class MapKey {
    private boolean beginIncluded;
    private final byte[] begin;

    private boolean endIncluded;
    private final byte[] end;

    public MapKey(boolean beginIncluded, byte[] begin, boolean endIncluded, byte[] end) {
        this.beginIncluded = beginIncluded;
        this.begin = begin;
        this.endIncluded = endIncluded;
        this.end = end;
    }

    public boolean isBeginIncluded() {
        return beginIncluded;
    }

    public byte[] getBegin() {
        return begin;
    }

    public boolean isEndIncluded() {
        return endIncluded;
    }

    public byte[] getEnd() {
        return end;
    }
}
