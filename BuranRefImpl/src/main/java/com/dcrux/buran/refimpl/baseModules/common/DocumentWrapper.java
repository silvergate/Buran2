package com.dcrux.buran.refimpl.baseModules.common;

import com.orientechnologies.orient.core.id.ORID;
import com.orientechnologies.orient.core.record.impl.ODocument;

/**
 * Buran.
 *
 * @author: ${USER} Date: 02.07.13 Time: 03:51
 */
public class DocumentWrapper {
    private final ODocument document;

    public DocumentWrapper(ODocument document) {
        this.document = document;
    }

    public final ODocument getDocument() {
        return document;
    }

    public final ORID getOrid() {
        return getDocument().getIdentity();
    }

    public final ONid getNid() {
        return ONid.fromDoc(getDocument());
    }

    public String getOrientClassName() {
        return getDocument().getClassName();
    }

    @Override
    public String toString() {
        return "DocumentWrapper{" +
                "document=" + document +
                '}';
    }
}
