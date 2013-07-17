package com.dcrux.buran.common.indexing.keyGen;

import com.sun.istack.internal.Nullable;

/**
 * Buran.
 *
 * @author: ${USER} Date: 09.07.13 Time: 23:38
 */
public class RangeIndexKeyGen implements IKeyGen {
    @Nullable
    private ISingleIndexKeyGen from;
    private boolean fromIncluded;
    @Nullable
    private ISingleIndexKeyGen to;
    private boolean toIncluded;

    private RangeIndexKeyGen(ISingleIndexKeyGen from, boolean fromIncluded, ISingleIndexKeyGen to,
            boolean toIncluded) {
        this.from = from;
        this.fromIncluded = fromIncluded;
        this.to = to;
        this.toIncluded = toIncluded;
    }

    private RangeIndexKeyGen() {
    }

    public static RangeIndexKeyGen between(ISingleIndexKeyGen from, ISingleIndexKeyGen to) {
        return new RangeIndexKeyGen(from, true, to, true);
    }

    public static RangeIndexKeyGen between(ISingleIndexKeyGen from, boolean fromIncluded,
            ISingleIndexKeyGen to, boolean toIncluded) {
        return new RangeIndexKeyGen(from, fromIncluded, to, toIncluded);
    }

    public static RangeIndexKeyGen from(ISingleIndexKeyGen from, boolean fromIncluded) {
        return new RangeIndexKeyGen(from, fromIncluded, null, false);
    }

    public static RangeIndexKeyGen to(ISingleIndexKeyGen to, boolean toIncluded) {
        return new RangeIndexKeyGen(null, false, to, toIncluded);
    }

    public static RangeIndexKeyGen from(ISingleIndexKeyGen from) {
        return new RangeIndexKeyGen(from, true, null, false);
    }

    public static RangeIndexKeyGen to(ISingleIndexKeyGen to) {
        return new RangeIndexKeyGen(null, false, to, true);
    }

    @Nullable
    public ISingleIndexKeyGen getFrom() {
        return from;
    }

    public boolean isFromIncluded() {
        return fromIncluded;
    }

    @Nullable
    public ISingleIndexKeyGen getTo() {
        return to;
    }

    public boolean isToIncluded() {
        return toIncluded;
    }
}
