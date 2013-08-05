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

    public static final Class<ExtNidLink> TYPE_EXT = ExtNidLink.class;
    public static final Class<Nid> TYPE_NID = Nid.class;
    public static final Class<NidVer> TYPE_NID_VER = NidVer.class;
    public static final Class<IncLinkTarget> TYPE_INC = IncLinkTarget.class;

    protected LinkTargetInc() {
        super("");
    }

    protected LinkTargetInc(Serializable data) {
        super(data);
    }

    public static LinkTargetInc extNid(ExtNidLink extNidLink) {
        return new LinkTargetInc(extNidLink);
    }

    public static LinkTargetInc nid(Nid nid) {
        return new LinkTargetInc(nid);
    }

    public static LinkTargetInc nidVer(NidVer nidVer) {
        return new LinkTargetInc(nidVer);
    }

    public static LinkTargetInc incVer(IncNid incNid) {
        return new LinkTargetInc(IncLinkTarget.versioned(incNid));
    }

    public static LinkTargetInc inc(IncNid incNid) {
        return new LinkTargetInc(IncLinkTarget.unversioned(incNid));
    }

}
