package com.dcrux.buran.common.fields.getter;

import com.dcrux.buran.common.link.LinkTarget;

/**
 * Buran.
 *
 * @author: ${USER} Date: 30.06.13 Time: 13:10
 */
public class FieldGetLinkTarget implements IUnfieldedDataGetter<LinkTarget> {
    public static final FieldGetLinkTarget SINGLETON = new FieldGetLinkTarget();

    private FieldGetLinkTarget() {
    }
}
