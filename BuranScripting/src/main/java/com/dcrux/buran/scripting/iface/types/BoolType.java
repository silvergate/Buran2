package com.dcrux.buran.scripting.iface.types;

import com.dcrux.buran.scripting.iface.IType;

/**
 * Buran.
 *
 * @author: ${USER} Date: 04.07.13 Time: 17:36
 */
public final class BoolType implements IType<Boolean> {

    private BoolType() {
    }

    public enum BoolRange {
        trueOnly(true, false),
        falseOnly(false, true),
        trueOrFalse(true, true);
        boolean canTrue;
        boolean canFalse;

        public boolean isCanTrue() {
            return canTrue;
        }

        public boolean isCanFalse() {
            return canFalse;
        }

        private BoolRange(boolean canTrue, boolean canFalse) {
            this.canTrue = canTrue;
            this.canFalse = canFalse;
        }
    }

    private BoolRange range;

    public BoolType(BoolRange range) {
        this.range = range;
    }

    public BoolRange getRange() {
        return range;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BoolType boolType = (BoolType) o;

        if (range != boolType.range) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return range.hashCode();
    }

    @Override
    public String toString() {
        return "BoolType{" +
                "range=" + range +
                '}';
    }

    public static BoolRange combineRanges(BoolRange r1, BoolRange r2) {
        boolean canTrue = r1.canTrue || r2.canTrue;
        boolean canFalse = r1.canFalse || r2.canFalse;
        if (canTrue && canFalse) {
            return BoolRange.trueOrFalse;
        } else if (canTrue) {
            return BoolRange.trueOnly;
        } else {
            return BoolRange.falseOnly;
        }
    }

    @Override
    public IType<Boolean> combineWith(IType<?> other) {
        final BoolType otherCast = (BoolType) other;
        return new BoolType(combineRanges(getRange(), otherCast.getRange()));
    }

    @Override
    public int getMemoryMaxMemoryRequirement() {
        return 1;
    }
}
