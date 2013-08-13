package com.dcrux.buran.common.query.indexingDef;

import com.dcrux.buran.common.classes.ClassId;
import com.dcrux.buran.common.fields.getter.FieldGetClassIds;
import com.dcrux.buran.common.fields.getter.IUnfieldedDataGetter;

import java.util.HashSet;

/**
 * Buran.
 *
 * @author: ${USER} Date: 06.08.13 Time: 11:37
 */
public class ClassIdsIndexingDef implements IIndexingDef<HashSet<ClassId>> {

    private boolean requiredForIndex;
    public static final FieldGetClassIds DEFAULT_GETTER = FieldGetClassIds.c();
    private IUnfieldedDataGetter<HashSet<ClassId>> getter;

    private ClassIdsIndexingDef() {
    }

    public static ClassIdsIndexingDef c() {
        return new ClassIdsIndexingDef(true, DEFAULT_GETTER);
    }

    public static ClassIdsIndexingDef c(IUnfieldedDataGetter<HashSet<ClassId>> getter) {
        return new ClassIdsIndexingDef(true, DEFAULT_GETTER);
    }

    public ClassIdsIndexingDef(boolean requiredForIndex,
            IUnfieldedDataGetter<HashSet<ClassId>> getter) {
        this.requiredForIndex = requiredForIndex;
        this.getter = getter;
    }

    @Override
    public IUnfieldedDataGetter<HashSet<ClassId>> getDataGetter() {
        return this.getter;
    }

    @Override
    public boolean requiredForIndex() {
        return this.requiredForIndex;
    }
}
