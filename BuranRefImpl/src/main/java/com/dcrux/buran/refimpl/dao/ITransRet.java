package com.dcrux.buran.refimpl.dao;

import com.orientechnologies.orient.core.db.document.ODatabaseDocument;

/**
 * Buran.
 *
 * @author: ${USER} Date: 29.06.13 Time: 14:03
 */
public interface ITransRet<T extends Object> {
    public T run(ODatabaseDocument db, IRunner runner) throws Throwable;
}
