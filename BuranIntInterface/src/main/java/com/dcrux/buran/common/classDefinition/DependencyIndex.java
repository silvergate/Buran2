package com.dcrux.buran.common.classDefinition;

import java.io.Serializable;

/**
 * Buran.
 *
 * @author: ${USER} Date: 06.08.13 Time: 21:56
 */
public class DependencyIndex implements Serializable {
    private short id;

    public DependencyIndex(short id) {
        this.id = id;
    }

    private DependencyIndex() {
    }

    public static DependencyIndex c(int id) {
        //TODO: Check short->int
        return new DependencyIndex((short) id);
    }

    public short getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DependencyIndex that = (DependencyIndex) o;

        if (id != that.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (int) id;
    }

    @Override
    public String toString() {
        return "DependencyIndex{" +
                "id=" + id +
                '}';
    }
}
