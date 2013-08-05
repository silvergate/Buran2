package com.dcrux.buran.common.link;

import com.dcrux.buran.common.ExtNid;
import com.dcrux.buran.common.ExtNidVer;
import com.dcrux.buran.common.Nid;
import com.dcrux.buran.common.NidVer;
import com.dcrux.buran.utils.WrappedAltType;

import java.io.Serializable;

/**
 * Buran.
 *
 * @author: ${USER} Date: 04.08.13 Time: 19:14
 */
public class LinkTarget extends WrappedAltType<LinkTarget> implements Serializable {

    public static final Class<ExtNid> TYPE_EXT_NID = ExtNid.class;
    public static final Class<ExtNidVer> TYPE_EXT_NID_VER = ExtNidVer.class;
    public static final Class<Nid> TYPE_NID = Nid.class;
    public static final Class<NidVer> TYPE_NID_VER = NidVer.class;

    protected LinkTarget() {
        super("");
    }

    protected LinkTarget(Serializable data) {
        super(data);
    }

    public static LinkTarget extNid(ExtNid extNid) {
        return new LinkTarget(extNid);
    }

    public static LinkTarget extNidVer(ExtNidVer extNidVer) {
        return new LinkTarget(extNidVer);
    }

    public static LinkTarget nid(Nid nid) {
        return new LinkTarget(nid);
    }

    public static LinkTarget nidVer(NidVer nidVer) {
        return new LinkTarget(nidVer);
    }
}
