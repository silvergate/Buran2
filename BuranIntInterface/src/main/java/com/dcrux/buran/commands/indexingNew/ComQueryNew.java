package com.dcrux.buran.commands.indexingNew;

import com.dcrux.buran.commands.Command;
import com.dcrux.buran.commands.indexing.QueryResult;
import com.dcrux.buran.query.queries.IQuery;

/**
 * Buran.
 *
 * @author: ${USER} Date: 08.08.13 Time: 21:34
 */
public class ComQueryNew extends Command<QueryResult> {
    private IQuery query;

    private ComQueryNew() {
    }

    public ComQueryNew(IQuery query) {
        super(exceptions());
        this.query = query;
    }

    public IQuery getQuery() {
        return query;
    }
}
