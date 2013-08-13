package com.dcrux.buran.demoGui;

import com.google.common.collect.Multimap;

/**
 * Buran.
 *
 * @author: ${USER} Date: 13.08.13 Time: 01:47
 */
public class Registry {

    private Multimap<Class<?>, Object> classObjectMultimap;

    public void register(Class<?> clazz, Object instance) {
        this.classObjectMultimap.put(clazz, instance);
    }
}
