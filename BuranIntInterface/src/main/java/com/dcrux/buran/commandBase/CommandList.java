package com.dcrux.buran.commandBase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Buran.
 *
 * @author: ${USER} Date: 02.07.13 Time: 14:14
 */
public class CommandList implements Serializable {

    private final List<ICommand> list = new ArrayList<ICommand>();

    public CommandIndex add(ICommand command) {
        this.list.add(command);
        return new CommandIndex(this.list.size() - 1);
    }

    public int getSize() {
        return this.list.size();
    }

    public ICommand getCommand(CommandIndex index) {
        return this.list.get(index.getIndex());
    }
}
