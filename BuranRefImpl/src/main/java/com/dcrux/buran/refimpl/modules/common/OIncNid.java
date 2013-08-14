package com.dcrux.buran.refimpl.modules.common;

import com.orientechnologies.orient.core.db.record.OIdentifiable;

/**
 * Buran.
 *
 * @author: ${USER} Date: 17.07.13 Time: 19:35
 */
public class OIncNid {
    private final OIdentifiable oIdentifiable;

    public OIncNid(OIdentifiable oIdentifiable) {
        this.oIdentifiable = oIdentifiable.getIdentity();
    }

    public OIdentifiable getoIdentifiable() {
        return oIdentifiable;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OIncNid oNidVer = (OIncNid) o;

        if (!oIdentifiable.equals(oNidVer.oIdentifiable)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return oIdentifiable.hashCode();
    }
}
