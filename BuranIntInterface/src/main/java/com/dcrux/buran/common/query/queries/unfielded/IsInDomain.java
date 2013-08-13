package com.dcrux.buran.common.query.queries.unfielded;

import com.dcrux.buran.common.domain.DomainId;
import com.dcrux.buran.common.query.indexingDef.DomainIdsIndexingDef;
import com.dcrux.buran.utils.ISerSet;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Buran.
 *
 * @author: ${USER} Date: 06.08.13 Time: 11:16
 */
public class IsInDomain
        implements ISimpleQuery<ISerSet<DomainId>, DomainIdsIndexingDef>, IConstantIndicator {

    public static final int MAX_ENTRIES = 16;

    private IsInDomain() {
    }

    public static enum LogicOp {
        any,
        all
    }

    private Set<DomainId> rhs;
    private LogicOp logicOp;
    private boolean constant = true;

    public IsInDomain(Set<DomainId> rhs, LogicOp logicOp, boolean constant) {
        if (rhs.size() == 0) {
            throw new IllegalArgumentException("rhs.size()==0");
        }
        if (rhs.size() > MAX_ENTRIES) {
            throw new IllegalArgumentException("rhs.size()>MAX_ENTRIES");
        }
        this.rhs = rhs;
        this.logicOp = logicOp;
        this.constant = constant;
    }

    public static IsInDomain contains(DomainId rhs) {
        return new IsInDomain(Collections.singleton(rhs), LogicOp.any, true);
    }

    @Override
    public boolean supports(DomainIdsIndexingDef indexingDef) {
        return true;
    }

    @Override
    public boolean matches(DomainIdsIndexingDef indexingDef, ISerSet<DomainId> value) {
        switch (this.logicOp) {
            case all:
                if (value.size() != this.rhs.size()) {
                    return false;
                }
                final Set<DomainId> valueCopy = new HashSet<DomainId>();
                valueCopy.addAll(value);
                valueCopy.addAll(this.rhs);
                return valueCopy.size() == value.size();
            case any:
                final Set<DomainId> valueCopy2 = new HashSet<DomainId>();
                valueCopy2.addAll(value);
                valueCopy2.retainAll(this.rhs);
                return !valueCopy2.isEmpty();
            default:
                throw new IllegalArgumentException("Unknown op");
        }
    }

    @Override
    public boolean isImplementationDefined() {
        return true;
    }

    public Set<DomainId> getRhs() {
        return rhs;
    }

    @Override
    public boolean isConstantQuery() {
        return this.constant;
    }

    public LogicOp getLogicOp() {
        return logicOp;
    }
}
