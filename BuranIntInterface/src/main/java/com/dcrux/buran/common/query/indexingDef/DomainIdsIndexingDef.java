package com.dcrux.buran.common.query.indexingDef;

import com.dcrux.buran.common.domain.DomainId;
import com.dcrux.buran.common.fields.getter.FieldGetDomainIds;
import com.dcrux.buran.common.fields.getter.IUnfieldedDataGetter;
import com.dcrux.buran.utils.ISerSet;

/**
 * Buran.
 *
 * @author: ${USER} Date: 06.08.13 Time: 11:37
 */
public class DomainIdsIndexingDef implements IIndexingDef<ISerSet<DomainId>> {

    private boolean requiredForIndex;
    public static final FieldGetDomainIds DEFAULT_GETTER = FieldGetDomainIds.c();
    private IUnfieldedDataGetter<ISerSet<DomainId>> getter;

    private DomainIdsIndexingDef() {
    }

    public static DomainIdsIndexingDef c() {
        return new DomainIdsIndexingDef(true, DEFAULT_GETTER);
    }

    public static DomainIdsIndexingDef c(IUnfieldedDataGetter<ISerSet<DomainId>> getter) {
        return new DomainIdsIndexingDef(true, DEFAULT_GETTER);
    }

    public DomainIdsIndexingDef(boolean requiredForIndex,
            IUnfieldedDataGetter<ISerSet<DomainId>> getter) {
        this.requiredForIndex = requiredForIndex;
        this.getter = getter;
    }

    @Override
    public IUnfieldedDataGetter<ISerSet<DomainId>> getDataGetter() {
        return this.getter;
    }

    @Override
    public boolean requiredForIndex() {
        return this.requiredForIndex;
    }
}
