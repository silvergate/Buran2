package com.dcrux.buran.query.queries.unfielded;

import com.dcrux.buran.common.classes.ClassId;
import com.dcrux.buran.query.indexingDef.ClassIdsIndexingDef;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Buran.
 *
 * @author: ${USER} Date: 06.08.13 Time: 11:16
 */
public class HasClass
        implements ISimpleQuery<HashSet<ClassId>, ClassIdsIndexingDef>, IConstantIndicator {

    public static enum LogicOp {
        any,
        all
    }

    private Set<ClassId> rhs;
    private LogicOp logicOp;
    private boolean constant = true;

    public HasClass(Set<ClassId> rhs, LogicOp logicOp, boolean constant) {
        if (rhs.size() == 0) {
            throw new IllegalArgumentException("rhs.size()==0");
        }
        this.rhs = rhs;
        this.logicOp = logicOp;
        this.constant = constant;
    }

    public static HasClass contains(ClassId rhs) {
        return new HasClass(Collections.singleton(rhs), LogicOp.any, true);
    }

    @Override
    public boolean supports(ClassIdsIndexingDef indexingDef) {
        return true;
    }

    @Override
    public boolean matches(ClassIdsIndexingDef indexingDef, HashSet<ClassId> value) {
        switch (this.logicOp) {
            case all:
                if (value.size() != this.rhs.size()) {
                    return false;
                }
                final Set<ClassId> valueCopy = new HashSet<ClassId>();
                valueCopy.addAll(value);
                valueCopy.addAll(this.rhs);
                return valueCopy.size() == value.size();
            case any:
                final Set<ClassId> valueCopy2 = new HashSet<ClassId>();
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

    public Set<ClassId> getRhs() {
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
