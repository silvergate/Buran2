package com.dcrux.buran.refimpl.modules.notifications;

import com.dcrux.buran.common.subscription.SubBlockId;
import com.dcrux.buran.common.subscription.SubId;

/**
 * Buran.
 *
 * @author: ${USER} Date: 17.07.13 Time: 03:48
 */
public class BlockAndSubId {
    private final SubBlockId blockId;
    private final SubId subId;

    public BlockAndSubId(SubBlockId blockId, SubId subId) {
        this.blockId = blockId;
        this.subId = subId;
    }

    public SubBlockId getBlockId() {
        return blockId;
    }

    public SubId getSubId() {
        return subId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BlockAndSubId that = (BlockAndSubId) o;

        if (!blockId.equals(that.blockId)) return false;
        if (!subId.equals(that.subId)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = blockId.hashCode();
        result = 31 * result + subId.hashCode();
        return result;
    }
}
