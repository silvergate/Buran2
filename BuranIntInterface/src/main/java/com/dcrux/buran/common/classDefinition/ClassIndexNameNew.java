package com.dcrux.buran.common.classDefinition;

import java.io.Serializable;

/**
 * Buran.
 *
 * @author: ${USER} Date: 08.07.13 Time: 10:09
 */
public class ClassIndexNameNew implements Serializable {
    private short id;

    public ClassIndexNameNew(short id) {
        this.id = id;
    }

    private ClassIndexNameNew() {
    }

    public short getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClassIndexNameNew that = (ClassIndexNameNew) o;

        if (id != that.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (int) id;
    }

    @Override
    public String toString() {
        return "ClassIndexNameNew{" +
                "id=" + id +
                '}';
    }
}
