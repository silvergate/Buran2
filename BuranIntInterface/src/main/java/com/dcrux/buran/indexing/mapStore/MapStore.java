package com.dcrux.buran.indexing.mapStore;

/**
 * Buran.
 *
 * @author: ${USER} Date: 08.07.13 Time: 09:44
 */
public class MapStore implements IMapStore {
    private final boolean rangeSupport;

    public MapStore(boolean rangeSupport) {
        this.rangeSupport = rangeSupport;
    }

    public boolean isRangeSupport() {
        return rangeSupport;
    }
}
