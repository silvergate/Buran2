package com.dcrux.buran.query.queries.fielded;

import com.dcrux.buran.query.queries.IQuery;
import com.dcrux.buran.query.queries.QueryTarget;
import com.dcrux.buran.query.queries.SubscriptionValidationException;
import com.dcrux.buran.query.queries.unfielded.ISimpleQuery;

import java.util.Set;

/**
 * Buran.
 *
 * @author: ${USER} Date: 06.08.13 Time: 22:30
 */
public class Query implements IOrQueryInput, IQueryOrMultifield, IQuery {
    private QueryTarget target;
    private ISimpleQuery<?, ?> def;

    public Query(QueryTarget target, ISimpleQuery<?, ?> def) {
        this.target = target;
        this.def = def;
    }

    public static Query c(QueryTarget target, ISimpleQuery<?, ?> def) {
        return new Query(target, def);
    }

    public QueryTarget getTarget() {
        return target;
    }

    public ISimpleQuery<?, ?> getDef() {
        return def;
    }

    @Override
    public void validateForSubscription(Set<QueryTarget> targets)
            throws SubscriptionValidationException {
        if (!this.def.isImplementationDefined()) {
            throw new SubscriptionValidationException();
        }
        targets.add(this.target);
    }
}
