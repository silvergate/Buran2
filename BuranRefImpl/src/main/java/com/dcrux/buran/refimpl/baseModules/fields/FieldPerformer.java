package com.dcrux.buran.refimpl.baseModules.fields;

import com.dcrux.buran.common.fields.getter.IUnfieldedDataGetter;
import com.dcrux.buran.common.fields.setter.IUnfieldedDataSetter;
import com.dcrux.buran.common.fields.typeDef.ITypeDef;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Buran.
 *
 * @author: ${USER} Date: 03.07.13 Time: 19:05
 */
public abstract class FieldPerformer<TFieldValue extends ITypeDef>
        implements IFieldPerformer<TFieldValue> {

    public static Set<Class<? extends IUnfieldedDataGetter<?>>> getters(Class<? extends
            IUnfieldedDataGetter<?>>... getters) {
        final Set<Class<? extends IUnfieldedDataGetter<?>>> set = new HashSet<>();
        set.addAll(Arrays.asList(getters));
        return set;
    }

    public static Set<Class<? extends IUnfieldedDataSetter>> setters(Class<? extends
            IUnfieldedDataSetter>... setter) {
        final Set<Class<? extends IUnfieldedDataSetter>> set = new HashSet<>();
        set.addAll(Arrays.asList(setter));
        return set;
    }

}
