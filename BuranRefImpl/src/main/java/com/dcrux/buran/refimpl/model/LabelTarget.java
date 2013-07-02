package com.dcrux.buran.refimpl.model;

import com.orientechnologies.orient.core.id.ORID;

/**
 * Buran.
 *
 * @author: ${USER} Date: 01.07.13 Time: 13:53
 */
public class LabelTarget {
    private final boolean incubation;
    private final ORID target;

    public LabelTarget(boolean incubation, ORID target) {
        this.incubation = incubation;
        this.target = target;
    }

    public boolean isIncubation() {
        return incubation;
    }

    public ORID getTarget() {
        return target;
    }
}
