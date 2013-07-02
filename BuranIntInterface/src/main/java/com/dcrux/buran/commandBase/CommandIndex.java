package com.dcrux.buran.commandBase;

import java.io.Serializable;

/**
 * Buran.
 *
 * @author: ${USER} Date: 02.07.13 Time: 14:14
 */
public final class CommandIndex implements Serializable {
    private final int index;

    public CommandIndex(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public String toString() {
        return "CommandIndex{" +
                "index=" + index +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CommandIndex that = (CommandIndex) o;

        if (index != that.index) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return index;
    }
}
