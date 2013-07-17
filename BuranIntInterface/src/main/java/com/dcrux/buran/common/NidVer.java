package com.dcrux.buran.common;

/**
 * Buran.
 *
 * @author: ${USER} Date: 17.07.13 Time: 10:02
 */
public class NidVer implements INidCommon {

    private String nidVer;

    public NidVer(String nidVer) {
        if ((nidVer.length() > INidCommon.MAX_LEN) || (nidVer.length() < INidCommon.MIN_LEN)) {
            throw new IllegalArgumentException(
                    "(nidVer.length()>INidCommon.MAX_LEN) || (nidVer.length()<INidCommon.MIN_LEN)");
        }
        this.nidVer = nidVer;
    }

    private NidVer() {
    }

    @Override
    public String getAsString() {
        return this.nidVer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NidVer nid1 = (NidVer) o;

        if (!nidVer.equals(nid1.nidVer)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return nidVer.hashCode();
    }
}
