package com.dcrux.buran.commands.indexing;

import java.io.Serializable;
import java.util.List;

/**
 * Buran.
 *
 * @author: ${USER} Date: 09.07.13 Time: 18:09
 */
public interface IQueryResult extends Serializable {
    List<String> getResults();
}
