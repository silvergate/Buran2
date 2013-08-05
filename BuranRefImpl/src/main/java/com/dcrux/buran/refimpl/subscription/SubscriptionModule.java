package com.dcrux.buran.refimpl.subscription;

import com.dcrux.buran.common.UserId;
import com.dcrux.buran.refimpl.baseModules.index.keyGen.KeyGenModule;
import com.dcrux.buran.refimpl.subscription.subRegistry.SubRegistry;
import com.sun.istack.internal.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Buran.
 *
 * @author: ${USER} Date: 17.07.13 Time: 01:49
 */
public class SubscriptionModule {
    private final KeyGenModule keyGenModule = new KeyGenModule(null);
    private final Map<UserId, SubRegistry> registriesUnsynced = new HashMap<>();
    private final Map<UserId, SubRegistry> registries =
            Collections.synchronizedMap(this.registriesUnsynced);

    public KeyGenModule getKeyGenModule() {
        return keyGenModule;
    }

    @Nullable
    public SubRegistry getRegistry(UserId receiver) {
        return this.registries.get(receiver);
    }

    public SubRegistry getOrCreateRegistry(UserId receiver) {
        final SubRegistry registry = getRegistry(receiver);
        if (registry == null) {
            synchronized (this) {
                final SubRegistry registry2 = getRegistry(receiver);
                if (registry2 == null) {
                    final SubRegistry registry3 = new SubRegistry(this);
                    this.registries.put(receiver, registry3);
                    return registry3;
                } else return registry2;
            }
        } else {
            return registry;
        }
    }
}
