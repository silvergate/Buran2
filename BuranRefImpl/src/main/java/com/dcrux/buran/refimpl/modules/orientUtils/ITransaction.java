package com.dcrux.buran.refimpl.modules.orientUtils;

import com.orientechnologies.orient.core.db.document.ODatabaseDocument;

/**
 * Buran.
 *
 * @author: ${USER} Date: 29.06.13 Time: 14:03
 */
public interface ITransaction {
    public void run(ODatabaseDocument db, IRunner runner) throws Throwable;
}
