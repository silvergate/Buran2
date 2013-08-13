package com.dcrux.buran.commands.subscription;

import com.dcrux.buran.commands.Command;
import com.dcrux.buran.common.query.queries.IQuery;
import com.dcrux.buran.common.subscription.SubBlockId;
import com.dcrux.buran.common.subscription.SubId;
import com.google.common.base.Optional;

/**
 * Buran.
 *
 * @author: ${USER} Date: 09.07.13 Time: 18:07
 */
public class ComAddSub extends Command<SubId> {
    private Optional<SubBlockId> blockId;
    private SubId subId;
    private IQuery query;

    public ComAddSub(Optional<SubBlockId> blockId, SubId subId, IQuery query) {
        super(exceptions());
        this.blockId = blockId;
        this.subId = subId;
        this.query = query;
    }

    public static ComAddSub c(SubId subId, IQuery query) {
        return new ComAddSub(Optional.<SubBlockId>absent(), subId, query);
    }

    private ComAddSub() {
    }

    public Optional<SubBlockId> getBlockId() {
        return blockId;
    }

    public SubId getSubId() {
        return subId;
    }

    public IQuery getQuery() {
        return query;
    }
}
