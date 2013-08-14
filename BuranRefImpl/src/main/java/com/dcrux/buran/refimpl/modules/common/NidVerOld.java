package com.dcrux.buran.refimpl.modules.common;

import com.dcrux.buran.common.Version;

import java.io.Serializable;

/**
 * Buran.
 *
 * @author: ${USER} Date: 30.06.13 Time: 23:33
 */
@Deprecated
public class NidVerOld implements Serializable {
    private INid nid;
    private Version version;

    public NidVerOld(INid nid, Version version) {
        this.nid = nid;
        this.version = version;
    }

    private NidVerOld() {
    }

    public INid getNid() {
        return nid;
    }

    public Version getVersion() {
        return version;
    }
}
