package com.dcrux.buran.common;

import java.io.Serializable;

/**
 * Buran.
 *
 * @author: ${USER} Date: 24.07.13 Time: 10:37
 */
public class ExtNid implements IExtNidOrNidVer, Serializable {
    private UserId account;
    private Nid nid;

    public ExtNid(UserId account, Nid nid) {
        this.account = account;
        this.nid = nid;
    }

    private ExtNid() {
    }

    public UserId getAccount() {
        return account;
    }

    public Nid getNid() {
        return nid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ExtNid extNid = (ExtNid) o;

        if (!account.equals(extNid.account)) return false;
        if (!nid.equals(extNid.nid)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = account.hashCode();
        result = 31 * result + nid.hashCode();
        return result;
    }
}
