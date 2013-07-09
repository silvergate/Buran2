package com.dcrux.buran.indexing.mapStore;

/**
 * Buran.
 *
 * @author: ${USER} Date: 08.07.13 Time: 09:44
 */
public class MapIndex implements IMapIndex {
    private final boolean rangeSupport;

    public MapIndex(boolean rangeSupport) {
        this.rangeSupport = rangeSupport;
    }

    public boolean isRangeSupport() {
        return rangeSupport;
    }
}
