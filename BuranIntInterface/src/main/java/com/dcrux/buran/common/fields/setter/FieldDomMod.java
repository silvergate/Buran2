package com.dcrux.buran.common.fields.setter;

import com.dcrux.buran.common.domain.DomainId;
import com.dcrux.buran.common.fields.typeDef.ITypeDef;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Buran.
 *
 * @author: ${USER} Date: 29.06.13 Time: 14:52
 */
public class FieldDomMod implements IUnfieldedDataSetter {

    public static enum Type {
        add,
        remove
    }

    private Type type;
    private Set<DomainId> domainIds;

    public static FieldDomMod remove(DomainId... domainIds) {
        final Set<DomainId> domainIdsSet = new HashSet<DomainId>();
        domainIdsSet.addAll(Arrays.asList(domainIds));
        return new FieldDomMod(Type.remove, domainIdsSet);
    }

    public static FieldDomMod add(DomainId... domainIds) {
        final Set<DomainId> domainIdsSet = new HashSet<DomainId>();
        domainIdsSet.addAll(Arrays.asList(domainIds));
        return new FieldDomMod(Type.add, domainIdsSet);
    }

    public FieldDomMod(Type type, Set<DomainId> domainIds) {
        this.type = type;
        this.domainIds = domainIds;
    }

    private FieldDomMod() {
    }

    public Type getType() {
        return type;
    }

    public Set<DomainId> getDomainIds() {
        return domainIds;
    }

    @Override
    public boolean canApplyTo(ITypeDef dataType) {
        return true;
    }
}
