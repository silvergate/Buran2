package com.dcrux.buran.demoGui.infrastructure.client;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;

/**
 * Buran.
 *
 * @author: ${USER} Date: 14.07.13 Time: 23:40
 */
public interface PluginIntent<TQuery extends IntentQuery, TResult extends Serializable>
        extends IsSerializable {

    Description getDescription();

    Description getDescription(TQuery query);

    IntentType<TQuery, TResult> getRef();

    TResult run(TQuery query);

    MatchLevel getMatchLevel(TQuery query);

    IntentRef<TQuery, TResult> getRefReal();
}
