package com.dcrux.buran.commands.subscription;

import com.dcrux.buran.commands.Command;
import com.dcrux.buran.common.subscription.SubBlockId;
import com.dcrux.buran.common.subscription.SubDefinition;
import com.dcrux.buran.common.subscription.SubId;

/**
 * Buran.
 *
 * @author: ${USER} Date: 09.07.13 Time: 18:07
 */
public class ComAddSub extends Command<SubId> {

    private SubBlockId blockId;
    private SubDefinition subDefinition;

    public ComAddSub(SubBlockId blockId, SubDefinition subDefinition) {
        super();
        this.blockId = blockId;
        this.subDefinition = subDefinition;
    }

    public static ComAddSub c(SubBlockId blockId, SubDefinition subDefinition) {
        return new ComAddSub(blockId, subDefinition);
    }

    private ComAddSub() {
    }

    public SubBlockId getBlockId() {
        return blockId;
    }

    public SubDefinition getSubDefinition() {
        return subDefinition;
    }
}
