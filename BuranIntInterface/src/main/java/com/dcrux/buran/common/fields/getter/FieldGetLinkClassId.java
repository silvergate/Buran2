package com.dcrux.buran.common.fields.getter;

import com.dcrux.buran.common.classes.ClassId;

/**
 * Buran.
 *
 * @author: ${USER} Date: 30.06.13 Time: 13:10
 */
public class FieldGetLinkClassId implements IUnfieldedDataGetter<ClassId> {
    public static final FieldGetLinkClassId SINGLETON = new FieldGetLinkClassId();

    private FieldGetLinkClassId() {
    }
}
