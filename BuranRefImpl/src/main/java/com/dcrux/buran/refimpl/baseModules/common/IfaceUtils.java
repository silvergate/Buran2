package com.dcrux.buran.refimpl.baseModules.common;

import com.dcrux.buran.common.IIncNid;
import com.dcrux.buran.common.INid;
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

    public static ONid getOincNid(IIncNid incNid) {
        if (incNid instanceof ONid) {
            return (ONid) incNid;
        } else {
            return new ONid(new ORecordId(incNid.getAsString()));
        }
    }
}
