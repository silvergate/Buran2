package com.dcrux.buran.commands.indexingNew;

import com.dcrux.buran.common.NidVer;
import com.dcrux.buran.utils.ISerList;

import java.io.Serializable;

/**
 * Buran.
 *
 * @author: ${USER} Date: 20.08.13 Time: 00:08
 */
public class QueryResultNew implements Serializable {
    private boolean noResult;
    private ISerList<NidVer> results;

    public QueryResultNew(boolean noResult, ISerList<NidVer> results) {
        this.noResult = noResult;
        this.results = results;
    }

    private QueryResultNew() {
    }

    public boolean isNoResult() {
        return noResult;
    }

    public ISerList<NidVer> getResults() {
        if (this.results == null) {
            throw new IllegalStateException("No results because limit exceeded.");
        }
        return results;
    }
}
