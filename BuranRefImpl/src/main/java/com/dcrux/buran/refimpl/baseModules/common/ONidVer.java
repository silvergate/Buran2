package com.dcrux.buran.refimpl.baseModules.common;

import com.orientechnologies.orient.core.id.ORID;

/**
 * Buran.
 *
 * @author: ${USER} Date: 17.07.13 Time: 19:35
 */
public class ONidVer {
    private final ORID oIdentifiable;

    public ONidVer(ORID oIdentifiable) {
        this.oIdentifiable = oIdentifiable;
    }

    public ORID getoIdentifiable() {
        return oIdentifiable;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ONidVer oNidVer = (ONidVer) o;

        if (!oIdentifiable.equals(oNidVer.oIdentifiable)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return oIdentifiable.hashCode();
    }

    @Override
    public String toString() {
        return "ONidVer{" +
                "oIdentifiable=" + oIdentifiable +
                '}';
    }
}
