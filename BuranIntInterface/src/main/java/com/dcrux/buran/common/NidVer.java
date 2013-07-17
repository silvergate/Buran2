package com.dcrux.buran.common;

import java.io.Serializable;

/**
 * Buran.
 *
 * @author: ${USER} Date: 30.06.13 Time: 23:33
 */
public class NidVer implements Serializable {
    private INid nid;
    private Version version;

    public NidVer(INid nid, Version version) {
        this.nid = nid;
        this.version = version;
    }

    private NidVer() {
    }

    public INid getNid() {
        return nid;
    }

    public Version getVersion() {
        return version;
    }
}
