package com.dcrux.buran.refimpl.baseModules.common;

import com.dcrux.buran.common.INid;
import com.orientechnologies.orient.core.id.ORID;
import com.orientechnologies.orient.core.id.ORecordId;
import com.orientechnologies.orient.core.record.impl.ODocument;

/**
 * Buran.
 *
 * @author: ${USER} Date: 29.06.13 Time: 15:29
 */
public class ONid implements INid, IIncNid {

    private final ORID recordId;

    public ONid(ORID recordId) {
        this.recordId = recordId;
    }

    public static ONid fromString(String oridStr) {
        return new ONid(new ORecordId(oridStr));
    }

    public static ONid fromDoc(ODocument document) {
        return new ONid(document.getIdentity());
    }

    @Override
    public String getAsString() {
        return this.recordId.toString();
    }

    public ORID getRecordId() {
        return recordId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ONid oNid = (ONid) o;

        if (!recordId.equals(oNid.recordId)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return recordId.hashCode();
    }

    @Override
    public String toString() {
        return "ONid{" +
                "recordId=" + recordId +
                '}';
    }
}
