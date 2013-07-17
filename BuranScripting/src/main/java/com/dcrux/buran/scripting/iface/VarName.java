package com.dcrux.buran.scripting.iface;

import java.io.Serializable;

/**
 * Buran.
 *
 * @author: ${USER} Date: 04.07.13 Time: 17:30
 */
public class VarName implements Serializable {
    private String name;

    private VarName() {
    }

    public String getName() {
        return name;
    }

    public static VarName c(String name) {
        return new VarName(name);
    }

    public VarName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VarName varName = (VarName) o;

        if (!name.equals(varName.name)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return "VarName{" +
                "name='" + name + '\'' +
                '}';
    }
}
