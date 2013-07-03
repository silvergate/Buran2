package com.dcrux.buran.refimpl.baseModules.fields;

import com.dcrux.buran.common.fields.typeDef.ITypeDef;
import com.sun.istack.internal.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Buran. THREAD-SAFE (except register method)
 *
 * @author: ${USER} Date: 03.07.13 Time: 19:08
 */
public class FieldPerformerRegistry {
    private final Map<Class<? extends ITypeDef>, IFieldPerformer> performers = new HashMap<>();
    private final Map<Class<? extends ITypeDef>, IFieldPerformer> performersSync =
            Collections.synchronizedMap(this.performers);

    public void register(IFieldPerformer<?> performer) {
        final Class<? extends ITypeDef> clazz = performer.supports();
        if (performersSync.containsKey(clazz)) {
            throw new IllegalArgumentException("Performer for this type already registered");
        }
        this.performersSync.put(performer.supports(), performer);
    }

    @Nullable
    public <TFieldType extends ITypeDef> IFieldPerformer<TFieldType> get(Class<TFieldType> clazz) {
        return this.performersSync.get(clazz);
    }
}
