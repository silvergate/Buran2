package com.dcrux.buran.refimpl.baseModules.newIndexing;

import com.dcrux.buran.common.query.indexingDef.IIndexingDef;
import com.dcrux.buran.refimpl.baseModules.newIndexing.processors.*;
import com.dcrux.buran.refimpl.baseModules.newIndexing.processorsIface.IIndexingDefImpl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Buran.
 *
 * @author: ${USER} Date: 08.08.13 Time: 14:10
 */
public class ProcessorsRegistry {

    private static final Map<Class<? extends IIndexingDef<?>>, IIndexingDefImpl<?, ?>> map =
            new HashMap<>();

    public <TJava extends Serializable, TIndexingDef extends IIndexingDef<TJava>>
    IIndexingDefImpl<TJava, TIndexingDef> get(
            Class<TIndexingDef> id) {
        return (IIndexingDefImpl<TJava, TIndexingDef>) map.get(id);
    }

    private static void register(IIndexingDefImpl<?, ?> deffImpl) {
        map.put(deffImpl.getSupportedType(), deffImpl);
    }

    static {
        register(new IntIndexingDefImpl());
        register(new StrAnalyzedIndexingDefImpl());
        register(new ClassIdIndexingDefImpl());
        register(new ClassIdsIndexingDefImpl());
        register(new DomainIdsIndexingDefImpl());
    }

    public IIndexingDefImpl<?, ?> getUnsafe(Class<? extends IIndexingDef> aClass) {
        return map.get(aClass);
    }
}
