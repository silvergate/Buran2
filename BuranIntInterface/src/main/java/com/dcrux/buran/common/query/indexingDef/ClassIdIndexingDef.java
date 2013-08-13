package com.dcrux.buran.common.query.indexingDef;

import com.dcrux.buran.common.classes.ClassId;
import com.dcrux.buran.common.fields.getter.FieldGetLinkClassId;
import com.dcrux.buran.common.fields.getter.IUnfieldedDataGetter;

/**
 * Buran.
 *
 * @author: ${USER} Date: 06.08.13 Time: 11:37
 */
public class ClassIdIndexingDef implements IIndexingDef<ClassId> {

    private boolean requiredForIndex;

    private ClassIdIndexingDef() {
    }

    public static ClassIdIndexingDef c() {
        return new ClassIdIndexingDef(true);
    }

    public static ClassIdIndexingDef cOptional() {
        return new ClassIdIndexingDef(false);
    }

    public ClassIdIndexingDef(boolean requiredForIndex) {
        this.requiredForIndex = requiredForIndex;
    }

    @Override
    public IUnfieldedDataGetter<ClassId> getDataGetter() {
        return FieldGetLinkClassId.SINGLETON;
    }

    @Override
    public boolean requiredForIndex() {
        return this.requiredForIndex;
    }
}
