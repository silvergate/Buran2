package com.dcrux.buran.common.inRelations;

import com.dcrux.buran.common.getterSetter.IDataGetter;
import com.dcrux.buran.common.inRelations.selector.IInRelSelector;
import com.dcrux.buran.common.inRelations.skipLimit.SkipLimit;
import com.dcrux.buran.common.inRelations.where.IInRelationWhere;

import java.io.Serializable;

/**
 * Buran.
 *
 * @author: ${USER} Date: 04.08.13 Time: 22:50
 */
public class InRealtionGetter<T extends Serializable> implements IDataGetter<InRelationResult<T>> {

    public static class Builder<T extends Serializable> {
        private Versioning versioning;
        private SkipLimit skipLimit = new SkipLimit(0, SkipLimit.LIMIT_MAX, true);
        private IInRelationWhere where;
        private IInRelSelector<T> selector;

        public Builder(IInRelSelector<T> selector) {
            this.selector = selector;
        }

        public Builder unversioned() {
            this.versioning = Versioning.unversioned;
            return this;
        }

        public Builder versioned() {
            this.versioning = Versioning.versioned;
            return this;
        }

        public Builder where(IInRelationWhere where) {
            this.where = where;
            return this;
        }

        public Builder limit(int limit) {
            this.skipLimit = new SkipLimit(this.skipLimit.getSkipCount(), limit,
                    this.skipLimit.isReturnPartialResults());
            return this;
        }

        public InRealtionGetter<T> get() {
            return new InRealtionGetter<T>(versioning, skipLimit, where, selector);
        }
    }

    private Versioning versioning;
    private SkipLimit skipLimit;
    private IInRelationWhere where;
    private IInRelSelector<T> selector;

    public InRealtionGetter(Versioning versioning, SkipLimit skipLimit, IInRelationWhere where,
            IInRelSelector<T> selector) {
        if (versioning == null) {
            throw new IllegalArgumentException("versioning==null");
        }
        if (skipLimit == null) {
            throw new IllegalArgumentException("skipLimit==null");
        }
        if (where == null) {
            throw new IllegalArgumentException("where==null");
        }
        if (selector == null) {
            throw new IllegalArgumentException("selector==null");
        }
        this.versioning = versioning;
        this.skipLimit = skipLimit;
        this.where = where;
        this.selector = selector;
    }

    public static <T extends Serializable> Builder<T> select(IInRelSelector<T> selector) {
        return new Builder<T>(selector);
    }

    private InRealtionGetter() {
    }

    public Versioning getVersioning() {
        return versioning;
    }

    public SkipLimit getSkipLimit() {
        return skipLimit;
    }

    public IInRelationWhere getWhere() {
        return where;
    }

    public IInRelSelector<T> getSelector() {
        return selector;
    }
}
