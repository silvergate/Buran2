package com.dcrux.buran.common.indexing.mapStore;

/**
 * Buran.
 *
 * @author: ${USER} Date: 08.07.13 Time: 09:44
 */
public class MapIndex implements IMapIndex {
    private boolean rangeSupport;

    public MapIndex(boolean rangeSupport) {
        this.rangeSupport = rangeSupport;
    }

    private MapIndex() {
    }

    public boolean isRangeSupport() {
        return rangeSupport;
    }
}
