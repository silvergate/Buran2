package com.dcrux.buran.common;

import java.io.Serializable;

/**
 * Buran.
 *
 * @author: ${USER} Date: 24.07.13 Time: 10:38
 */
public class ExtNidVer implements IExtNidOrNidVer, Serializable {
    private UserId account;
    private NidVer nidVer;

    public ExtNidVer(UserId account, NidVer nidVer) {
        this.account = account;
        this.nidVer = nidVer;
    }

    private ExtNidVer() {
    }

    public UserId getAccount() {
        return account;
    }

    public NidVer getNidVer() {
        return nidVer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ExtNidVer extNidVer = (ExtNidVer) o;

        if (!account.equals(extNidVer.account)) return false;
        if (!nidVer.equals(extNidVer.nidVer)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = account.hashCode();
        result = 31 * result + nidVer.hashCode();
        return result;
    }
}
