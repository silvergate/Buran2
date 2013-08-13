package com.dcrux.buran.common.query.queries.unfielded;

import com.dcrux.buran.common.classes.ClassId;
import com.dcrux.buran.common.query.indexingDef.ClassIdIndexingDef;

/**
 * Buran.
 *
 * @author: ${USER} Date: 06.08.13 Time: 11:16
 */
public class IsClass implements ISimpleQuery<ClassId, ClassIdIndexingDef>, IConstantIndicator {
    private ClassId rhs;
    private boolean constant = true;

    public IsClass(ClassId rhs) {
        this.rhs = rhs;
    }

    private IsClass() {
    }

    @Override
    public boolean supports(ClassIdIndexingDef indexingDef) {
        return true;
    }

    @Override
    public boolean matches(ClassIdIndexingDef indexingDef, ClassId value) {
        return this.rhs.equals(value);
    }

    @Override
    public boolean isImplementationDefined() {
        return true;
    }

    public ClassId getRhs() {
        return rhs;
    }

    @Override
    public boolean isConstantQuery() {
        return this.constant;
    }
}
