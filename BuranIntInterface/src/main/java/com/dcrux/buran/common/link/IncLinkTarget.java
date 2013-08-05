package com.dcrux.buran.common.link;

import com.dcrux.buran.common.IncNid;

import java.io.Serializable;

/**
 * Buran.
 *
 * @author: ${USER} Date: 05.08.13 Time: 00:59
 */
public class IncLinkTarget implements Serializable {
    private boolean versioned;
    private IncNid incNid;

    public IncLinkTarget(boolean versioned, IncNid incNid) {
        this.versioned = versioned;
        this.incNid = incNid;
    }

    public static IncLinkTarget versioned(IncNid incNid) {
        return new IncLinkTarget(true, incNid);
    }

    public static IncLinkTarget unversioned(IncNid incNid) {
        return new IncLinkTarget(false, incNid);
    }

    private IncLinkTarget() {
    }

    public boolean isVersioned() {
        return versioned;
    }

    public IncNid getIncNid() {
        return incNid;
    }
}
