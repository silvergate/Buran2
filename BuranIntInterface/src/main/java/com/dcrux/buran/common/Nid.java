package com.dcrux.buran.common;

import com.dcrux.buran.utils.AltType;

/**
 * Buran.
 *
 * @author: ${USER} Date: 17.07.13 Time: 10:02
 */
public class Nid extends AltType<INidOrNidVer> implements INidOrNidVer, INidCommon {

    private String nid;

    public Nid(String nid) {
        if ((nid.length() > INidCommon.MAX_LEN) || (nid.length() < INidCommon.MIN_LEN)) {
            throw new IllegalArgumentException(
                    "(nid.length()>INidCommon.MAX_LEN) || (nid.length()<INidCommon.MIN_LEN)");
        }
        this.nid = nid;
    }

    private Nid() {
    }

    @Override
    public String getAsString() {
        return this.nid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Nid nid1 = (Nid) o;

        if (!nid.equals(nid1.nid)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return nid.hashCode();
    }
}
