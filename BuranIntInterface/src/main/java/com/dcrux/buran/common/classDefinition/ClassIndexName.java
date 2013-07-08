package com.dcrux.buran.common.classDefinition;

import java.io.Serializable;

/**
 * Buran.
 *
 * @author: ${USER} Date: 08.07.13 Time: 10:09
 */
public class ClassIndexName implements Serializable {
    public static final int MIN_LEN = 2;
    public static final int MAX_LEN = 32;
    private final String name;

    public ClassIndexName(String name) {
        if ((name.length() < MIN_LEN) || (name.length() > MAX_LEN)) {
            throw new IllegalArgumentException(
                    "(name.length()<MIN_LEN) || (name.length()" + ">MAX_LEN)");
        }
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClassIndexName that = (ClassIndexName) o;

        if (!name.equals(that.name)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return "ClassIndexName{" +
                "name='" + name + '\'' +
                '}';
    }
}
