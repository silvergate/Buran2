package com.dcrux.buran.common;

/**
 * Buran.
 *
 * @author: ${USER} Date: 17.07.13 Time: 10:15
 */
public class IncNid implements INidCommon {
    private String incNid;

    @Override
    public String getAsString() {
        return this.incNid;
    }

    public IncNid(String incNid) {
        if ((incNid.length() > INidCommon.MAX_LEN) || (incNid.length() < INidCommon.MIN_LEN)) {
            throw new IllegalArgumentException(
                    "(incNid.length()>INidCommon.MAX_LEN) || (incNid.length()<INidCommon.MIN_LEN)");
        }
        this.incNid = incNid;
    }

    private IncNid() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IncNid incNid1 = (IncNid) o;

        if (!incNid.equals(incNid1.incNid)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return incNid.hashCode();
    }
}
