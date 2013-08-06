package com.dcrux.buran.queryDsl.queries.fielded;

import com.dcrux.buran.queryDsl.queries.IQuery;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Buran.
 *
 * @author: ${USER} Date: 06.08.13 Time: 22:37
 */
public class AndQuery implements IOrQueryInput, IQuery {

    public static final int MAX_ELEMENTS = 6;

    private Set<IQueryOrMultifield> queries = new HashSet<IQueryOrMultifield>();

    public Set<IQueryOrMultifield> getQueries() {
        return Collections.unmodifiableSet(queries);
    }

    public void add(IQueryOrMultifield query) {
        if (this.queries.size() >= MAX_ELEMENTS) {
            throw new IllegalArgumentException("Number of queries exceeded");
        }
        this.queries.add(query);
    }
}
