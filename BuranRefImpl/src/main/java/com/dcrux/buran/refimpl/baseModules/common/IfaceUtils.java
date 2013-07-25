package com.dcrux.buran.refimpl.baseModules.common;

import com.dcrux.buran.common.INid;
import com.dcrux.buran.common.IncNid;
import com.dcrux.buran.common.Nid;
import com.dcrux.buran.common.NidVer;
import com.orientechnologies.orient.core.id.ORecordId;

/**
 * Buran.
 *
 * @author: ${USER} Date: 29.06.13 Time: 15:36
 */
public class IfaceUtils {
    public static ONid getONid(INid nid) {
        if (nid instanceof ONid) {
            return (ONid) nid;
        } else {
            return new ONid(new ORecordId(nid.getAsString()));
        }
    }

    public static ONid getONid(Nid nid) {
        return new ONid(new ORecordId(nid.getAsString()));
    }

    public static ONid getOincNid(IncNid nid) {
        return new ONid(new ORecordId(nid.getAsString()));
    }

    public static ONid getOincNid(IIncNid incNid) {
        if (incNid instanceof ONid) {
            return (ONid) incNid;
        } else {
            return new ONid(new ORecordId(incNid.getAsString()));
        }
    }

    public static NidVer toOutput(ONidVer oNidVer) {
        return new NidVer(oNidVer.getoIdentifiable().toString());
    }

    public static Nid toOutput(INid nid) {
        return new Nid(nid.getAsString());
    }

    public static IncNid toOutput(OIncNid oIncNid) {
        return new IncNid(oIncNid.getoIdentifiable().toString());
    }


    public static ONidVer toInput(NidVer nidVer) {
        return new ONidVer(new ORecordId(nidVer.getAsString()));
    }
}
