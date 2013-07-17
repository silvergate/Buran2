package com.dcrux.buran.scripting.iface.types;

import com.dcrux.buran.scripting.iface.IType;

import java.util.*;

/**
 * Buran.
 *
 * @author: ${USER} Date: 05.07.13 Time: 08:41
 */
public class AltType implements IType<Object> {

    private AltType() {
    }

    public static AltType c(Set<IType<?>> types) {
        final Set<IType<?>> allTypes = new HashSet<>();
        for (final IType<?> type : types) {
            extractSingles(type, allTypes);
        }
        return new AltType(getCombinedMap(allTypes));
    }

    public static AltType c(IType<?>... types) {
        final Set<IType<?>> allTypes = new HashSet<>();
        for (final IType<?> type : types) {
            allTypes.add(type);
        }
        return c(allTypes);
    }

    private AltType(Map<Class<?>, IType<?>> alternatives) {
        this.alternatives = alternatives;
    }

    private Map<Class<?>, IType<?>> alternatives;

    private static void extractSingles(IType<?> type, Set<IType<?>> outResult) {
        if (type instanceof AltType) {
            for (final IType<?> inner : ((AltType) type).getAlternatives()) {
                extractSingles(inner, outResult);
            }
        } else {
            outResult.add(type);
        }
    }

    private static Map<Class<?>, IType<?>> getCombinedMap(Set<IType<?>> types) {
        final Map<Class<?>, IType<?>> newMap = new HashMap<>();

        for (final IType<?> type : types) {
            if (!newMap.containsKey(type.getClass())) {
                newMap.put(type.getClass(), type);
            } else {
                final IType<?> existing = newMap.get(type.getClass());
                final IType<?> combined = existing.combineWith(type);
                newMap.put(type.getClass(), combined);
            }
        }
        return newMap;
    }

    public Collection<IType<?>> getAlternatives() {
        return Collections.unmodifiableCollection(this.alternatives.values());
    }

    @Override
    public IType<Object> combineWith(IType<?> other) {
        final AltType otherCast = (AltType) other;
        final Set<IType<?>> allTypes = new HashSet<>();
        extractSingles(this, allTypes);
        extractSingles(otherCast, allTypes);
        return new AltType(getCombinedMap(allTypes));
    }

    @Override
    public int getMemoryMaxMemoryRequirement() {
        int max = 0;
        for (final IType<?> type : this.alternatives.values()) {
            max = Math.max(type.getMemoryMaxMemoryRequirement(), max);
        }
        return max;
    }
}
