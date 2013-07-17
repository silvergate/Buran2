package com.dcrux.buran.refimpl.subscription.subRegistry;

import com.dcrux.buran.common.subscription.SubBlockId;
import com.dcrux.buran.common.subscription.SubId;

/**
 * Buran.
 *
 * @author: ${USER} Date: 17.07.13 Time: 02:02
 */
public class SubFull {
    private final SubBlockId subBlockId;
    private final SubId subId;

    public SubFull(SubBlockId subBlockId, SubId subId) {
        this.subBlockId = subBlockId;
        this.subId = subId;
    }

    public SubBlockId getSubBlockId() {
        return subBlockId;
    }

    public SubId getSubId() {
        return subId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SubFull subFull = (SubFull) o;

        if (!subBlockId.equals(subFull.subBlockId)) return false;
        if (!subId.equals(subFull.subId)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = subBlockId.hashCode();
        result = 31 * result + subId.hashCode();
        return result;
    }
}
