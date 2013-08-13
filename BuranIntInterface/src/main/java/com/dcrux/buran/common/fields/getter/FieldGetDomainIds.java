package com.dcrux.buran.common.fields.getter;

import com.dcrux.buran.common.domain.DomainId;
import com.dcrux.buran.utils.ISerSet;

/**
 * Buran.
 *
 * @author: ${USER} Date: 30.06.13 Time: 13:10
 */
public class FieldGetDomainIds implements IUnfieldedDataGetter<ISerSet<DomainId>> {

    public static final FieldGetDomainIds SINGLETON = new FieldGetDomainIds();

    private FieldGetDomainIds() {
    }

    public static FieldGetDomainIds c() {
        return SINGLETON;
    }
}
