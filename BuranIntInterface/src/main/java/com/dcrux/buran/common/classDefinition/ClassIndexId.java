package com.dcrux.buran.common.classDefinition;

import java.io.Serializable;

/**
 * Buran.
 *
 * @author: ${USER} Date: 08.07.13 Time: 10:09
 */
public class ClassIndexId implements Serializable {

    public static final ClassIndexId DEFAULT = new ClassIndexId((short) 0);
    public static final ClassIndexId IDX_0 = DEFAULT;
    public static final ClassIndexId IDX_1 = new ClassIndexId((short) 1);
    public static final ClassIndexId IDX_2 = new ClassIndexId((short) 2);
    public static final ClassIndexId IDX_3 = new ClassIndexId((short) 3);

    private short id;

    public ClassIndexId(short id) {
        if ((id < 0) || (id > 3)) {
            throw new IllegalArgumentException("((id<0) || (id>3))");
        }
        this.id = id;
    }

    private ClassIndexId() {
    }

    public short getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClassIndexId that = (ClassIndexId) o;

        if (id != that.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (int) id;
    }

    @Override
    public String toString() {
        return "ClassIndexId{" +
                "id=" + id +
                '}';
    }
}
