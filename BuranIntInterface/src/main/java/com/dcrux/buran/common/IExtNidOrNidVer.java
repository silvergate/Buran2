package com.dcrux.buran.common;

import com.dcrux.buran.utils.IAltType;

import java.io.Serializable;

/**
 * Buran.
 *
 * @author: ${USER} Date: 24.07.13 Time: 10:48
 */
public interface IExtNidOrNidVer extends Serializable, IAltType<IExtNidOrNidVer> {
    public static final Class<ExtNid> TYPE_EXT_NID = ExtNid.class;
    public static final Class<ExtNidVer> TYPE_EXT_NID_VER = ExtNidVer.class;
}
