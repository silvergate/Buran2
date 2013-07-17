package com.dcrux.buran.refimpl.subscription.subRegistry;

import com.dcrux.buran.common.classDefinition.ClassIndexName;
import com.dcrux.buran.common.classes.ClassId;
import com.dcrux.buran.common.indexing.KeyComparator;
import com.dcrux.buran.common.subscription.SubBlockId;
import com.dcrux.buran.common.subscription.SubDefinition;
import com.dcrux.buran.common.subscription.SubId;
import com.dcrux.buran.refimpl.baseModules.common.Module;
import com.dcrux.buran.refimpl.baseModules.index.keyGen.MapKey;
import com.dcrux.buran.refimpl.subscription.SubscriptionModule;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.sun.istack.internal.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * Buran.
 *
 * @author: ${USER} Date: 17.07.13 Time: 01:52
 */
public class SubRegistry extends Module<SubscriptionModule> {

    private final Multimap<SubBlockId, SubId> blocks = HashMultimap.create();
    private final Map<SubBlockId, Integer> blocksSubIdCounter = new HashMap<>();

    private final Map<ClassIdAndIndex, Map<MapKey, SubFull>> accessMap = new HashMap<>();

    public SubRegistry(SubscriptionModule subscriptionModule) {
        super(subscriptionModule);
    }

    public synchronized SubId addSubscription(SubBlockId subBlockId, SubDefinition subDefinition) {
        /* Get subscription id */
        final int subId;
        if (this.blocksSubIdCounter.containsKey(subBlockId)) {
            subId = this.blocksSubIdCounter.get(subBlockId) + 1;
        } else {
            subId = 0;
            this.blocksSubIdCounter.put(subBlockId, subId);
        }
        final SubId subIdObj = new SubId(subId);

        /* Put to blocks */
        this.blocks.put(subBlockId, subIdObj);

        /* Add to access map */
        final ClassIdAndIndex classIdAndIndex =
                new ClassIdAndIndex(subDefinition.getClassId(), subDefinition.getClassIndexName());
        Map<MapKey, SubFull> innerAccessMap = this.accessMap.get(classIdAndIndex);
        if (innerAccessMap == null) {
            innerAccessMap = new HashMap<>();
            this.accessMap.put(classIdAndIndex, innerAccessMap);
        }
        MapKey mapKey = getBase().getKeyGenModule().generate(subDefinition.getKeyGen());
        innerAccessMap.put(mapKey, new SubFull(subBlockId, subIdObj));

        return subIdObj;
    }

    @Nullable
    public synchronized Multimap<SubBlockId, SubId> getByMapValue(ClassId classId,
            ClassIndexName classIndexName, byte[] mapValue) {
        final Map<MapKey, SubFull> subs =
                this.accessMap.get(new ClassIdAndIndex(classId, classIndexName));
        if (subs == null) {
            return null;
        }

        Multimap<SubBlockId, SubId> results = null;
        for (final Map.Entry<MapKey, SubFull> subbDefEntry : subs.entrySet()) {
            final MapKey mapKey = subbDefEntry.getKey();

            /* Check begin */
            final boolean beginOk;
            if (mapKey.getBegin() != null) {
                final int beginResult = KeyComparator.compareTo(mapValue, mapKey.getBegin());
                beginOk = (beginResult == 0 && mapKey.isBeginIncluded()) || (beginResult > 0);
            } else {
                beginOk = true;
            }

            if (beginOk) {

                /* Check end */
                final boolean endOk;
                if (mapKey.getEnd() != null) {
                    final int endResult = KeyComparator.compareTo(mapValue, mapKey.getEnd());
                    endOk = (endResult == 0 && mapKey.isEndIncluded()) || (endResult < 0);
                } else {
                    endOk = true;
                }

                if (endOk) {
                    final SubFull subFull = subbDefEntry.getValue();
                    if (results == null) {
                        results = HashMultimap.create();
                    }
                    results.put(subFull.getSubBlockId(), subFull.getSubId());
                }
            }
        }

        return results;
    }
}
