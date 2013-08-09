package com.dcrux.buran.commands.indexing;

import com.dcrux.buran.common.NidVer;

import java.io.Serializable;
import java.util.List;

/**
 * Buran.
 *
 * @author: ${USER} Date: 09.07.13 Time: 18:09
 */
@Deprecated
public class QueryResult implements Serializable {
    private boolean limited;
    private List<NidVer> results;

    public QueryResult(boolean limited, List<NidVer> results) {
        this.limited = limited;
        this.results = results;
    }

    private QueryResult() {
    }

    public List<NidVer> getResults() {
        return this.results;
    }
}
