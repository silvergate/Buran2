package com.dcrux.buran.queryDsl.queries.fielded;

import com.dcrux.buran.queryDsl.queries.IQuery;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Buran.
 *
 * @author: ${USER} Date: 06.08.13 Time: 22:42
 */
public class OrQuery implements IQuery {
    public static final int MAX_ELEMENTS = 8;

    private Set<IOrQueryInput> queries = new HashSet<IOrQueryInput>();

    public Set<IOrQueryInput> getQueries() {
        return Collections.unmodifiableSet(this.queries);
    }

    private int getNumOfElements(IOrQueryInput query) {
        if (query instanceof AndQuery) {
            return ((AndQuery) query).getQueries().size();
        }
        return 1;
    }

    private int getNumOfElements(Set<IOrQueryInput> queries) {
        int elements = 0;
        for (IOrQueryInput query : queries) {
            elements += getNumOfElements(query);
        }
        return elements;
    }

    public void add(IOrQueryInput query) {
        int numOfElements = getNumOfElements(this.queries);
        int newElements = getNumOfElements(query);
        if (numOfElements + newElements > MAX_ELEMENTS) {
            throw new IllegalArgumentException("Max number of query elements exceeded");
        }
        this.queries.add(query);
    }

}
