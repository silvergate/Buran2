package com.dcrux.buran.common.classes;

import java.io.Serializable;

/**
 * Buran.
 *
 * @author: ${USER} Date: 29.06.13 Time: 15:40
 */
public final class ClassId implements Serializable {
    private final long id;

    public ClassId(long id) {
        this.id = id;
    }

    public static ClassId c(long id) {
        return new ClassId(id);
    }

    public long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClassId classId = (ClassId) o;

        if (id != classId.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public String toString() {
        return "ClassId{" +
                "id=" + id +
                '}';
    }
}
