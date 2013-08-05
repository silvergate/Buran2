package com.dcrux.buran.common.link;

import com.dcrux.buran.common.IncNid;
import com.dcrux.buran.common.Nid;
import com.dcrux.buran.common.NidVer;
import com.dcrux.buran.utils.WrappedAltType;

import java.io.Serializable;

/**
 * Buran.
 *
 * @author: ${USER} Date: 05.08.13 Time: 00:17
 */
public class LinkTargetInc extends WrappedAltType<Serializable> implements Serializable {

    public static final Class<ExtNidLinkTarget> TYPE_EXT = ExtNidLinkTarget.class;
    public static final Class<Nid> TYPE_NID = Nid.class;
    public static final Class<NidVerLinkTarget> TYPE_NID_VER = NidVerLinkTarget.class;
    public static final Class<IncLinkTarget> TYPE_INC = IncLinkTarget.class;

    protected LinkTargetInc() {
        super("");
    }

    protected LinkTargetInc(Serializable data) {
        super(data);
    }

    public static LinkTargetInc extNid(ExtNidLinkTarget extNidLinkTarget) {
        return new LinkTargetInc(extNidLinkTarget);
    }

    public static LinkTargetInc nid(Nid nid) {
        return new LinkTargetInc(nid);
    }

    public static LinkTargetInc nidVer(NidVer nidVer) {
        return new LinkTargetInc(NidVerLinkTarget.versioned(nidVer));
    }

    public static LinkTargetInc nidVerUnversioned(NidVer nidVer) {
        return new LinkTargetInc(NidVerLinkTarget.unversioned(nidVer));
    }

    public static LinkTargetInc incVer(IncNid incNid) {
        return new LinkTargetInc(IncLinkTarget.versioned(incNid));
    }

    public static LinkTargetInc inc(IncNid incNid) {
        return new LinkTargetInc(IncLinkTarget.unversioned(incNid));
    }

}
