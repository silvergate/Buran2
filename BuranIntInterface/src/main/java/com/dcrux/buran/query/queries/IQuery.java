package com.dcrux.buran.query.queries;

import java.io.Serializable;
import java.util.Set;

/**
 * Buran.
 *
 * @author: ${USER} Date: 06.08.13 Time: 22:51
 */
public interface IQuery extends Serializable {
    void validateForSubscription(Set<QueryTarget> targets) throws SubscriptionValidationException;
}
