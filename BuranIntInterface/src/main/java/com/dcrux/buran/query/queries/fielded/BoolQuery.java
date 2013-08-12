package com.dcrux.buran.query.queries.fielded;

import com.dcrux.buran.query.queries.IQuery;
import com.dcrux.buran.query.queries.QueryTarget;
import com.dcrux.buran.query.queries.SubscriptionValidationException;
import com.dcrux.buran.query.queries.unfielded.IConstantIndicator;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Buran.
 *
 * @author: ${USER} Date: 06.08.13 Time: 22:42
 */
public class BoolQuery implements IQuery, IConstantIndicator {

    @Override
    public void validateForSubscription(Set<QueryTarget> targets)
            throws SubscriptionValidationException {
        for (final Entry entry : this.queries) {
            entry.getOrQueryInput().validateForSubscription(targets);
        }
    }

    public static class Entry {
        private BooleanLogic logic;
        private IOrQueryInput orQueryInput;

        public Entry(BooleanLogic logic, IOrQueryInput orQueryInput) {
            this.logic = logic;
            this.orQueryInput = orQueryInput;
        }

        public BooleanLogic getLogic() {
            return logic;
        }

        public IOrQueryInput getOrQueryInput() {
            return orQueryInput;
        }
    }

    public static final int MAX_ELEMENTS = 8;
    private boolean constant;

    private Set<Entry> queries = new HashSet<Entry>();

    public Set<Entry> getQueries() {
        return Collections.unmodifiableSet(this.queries);
    }

    private int getNumOfElements(Set<Entry> queries) {
        return queries.size();
    }

    private void validateSize() {
        int numOfElements = getNumOfElements(this.queries);
        if (numOfElements > MAX_ELEMENTS) {
            throw new IllegalArgumentException("Max number of query elements exceeded");
        }
    }

    public static BoolQuery c() {
        return new BoolQuery();
    }

    public BoolQuery must(IOrQueryInput query) {
        validateSize();
        this.queries.add(new Entry(BooleanLogic.must, query));
        return this;
    }

    public BoolQuery mustNot(IOrQueryInput query) {
        validateSize();
        this.queries.add(new Entry(BooleanLogic.mustNot, query));
        return this;
    }

    public BoolQuery should(IOrQueryInput query) {
        validateSize();
        this.queries.add(new Entry(BooleanLogic.should, query));
        return this;
    }

    @Override
    public boolean isConstantQuery() {
        return this.constant;
    }
}
