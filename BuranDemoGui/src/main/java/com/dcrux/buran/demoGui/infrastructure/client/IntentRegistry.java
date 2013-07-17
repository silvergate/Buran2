package com.dcrux.buran.demoGui.infrastructure.client;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Buran.
 *
 * @author: ${USER} Date: 15.07.13 Time: 00:01
 */
public class IntentRegistry {

    public static final IntentRegistry SINGLETON = new IntentRegistry();

    private Multimap<IntentType, PluginIntent<?, ?>> intents = HashMultimap.create();
    private Map<IntentRef, PluginIntent<?, ?>> intentsByRef =
            new HashMap<IntentRef, PluginIntent<?, ?>>();

    public void register(PluginIntent intent) {
        final IntentType ref = intent.getRef();
        this.intents.put(ref, intent);
        this.intentsByRef.put(intent.getRefReal(), intent);
    }

    public <TQuery extends IntentQuery, TResult extends Serializable> PluginIntent<TQuery,
            TResult> getBest(
            IntentType<TQuery, TResult> type, TQuery query) {
        final Collection<PluginIntent<?, ?>> matchingIntents = this.intents.get(type);
        int level = Integer.MIN_VALUE;
        PluginIntent bestIntent = null;
        for (final PluginIntent matchingIntent : matchingIntents) {
            MatchLevel localLevel = matchingIntent.getMatchLevel(query);
            if (localLevel.getLevel() > level) {
                level = localLevel.getLevel();
                bestIntent = matchingIntent;
            }
        }
        return bestIntent;
    }

    public <TQuery extends IntentQuery, TResult extends Serializable> Collection<PluginIntent<?,
            ?>> getAll(
            IntentType<TQuery, TResult> type, TQuery query) {
        final Collection<PluginIntent<?, ?>> matchingIntents = this.intents.get(type);
        return matchingIntents;
    }

    public <TQuery extends IntentQuery, TResult extends Serializable> PluginIntent<?, ?> get(
            IntentRef<TQuery, TResult> ref) {
        final PluginIntent<?, ?> intent = this.intentsByRef.get(ref);
        return (PluginIntent<?, ?>) intent;
    }
}
