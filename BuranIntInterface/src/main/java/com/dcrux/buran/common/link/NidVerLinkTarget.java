package com.dcrux.buran.common.link;

import com.dcrux.buran.common.NidVer;

import java.io.Serializable;

/**
 * Buran.
 *
 * @author: ${USER} Date: 05.08.13 Time: 09:54
 */
public class NidVerLinkTarget implements Serializable {
    private NidVer nidVer;
    private boolean versioned;

    public NidVerLinkTarget(NidVer nidVer, boolean versioned) {
        this.nidVer = nidVer;
        this.versioned = versioned;
    }

    public static NidVerLinkTarget versioned(NidVer nidVer) {
        return new NidVerLinkTarget(nidVer, true);
    }

    public static NidVerLinkTarget unversioned(NidVer nidVer) {
        return new NidVerLinkTarget(nidVer, false);
    }

    private NidVerLinkTarget() {
    }

    public NidVer getNidVer() {
        return nidVer;
    }

    public boolean isVersioned() {
        return versioned;
    }
}
