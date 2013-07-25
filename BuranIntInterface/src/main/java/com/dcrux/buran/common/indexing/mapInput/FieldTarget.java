package com.dcrux.buran.common.indexing.mapInput;

import com.dcrux.buran.common.fields.FieldIndex;
import com.dcrux.buran.common.fields.getter.FieldGetPrim;
import com.dcrux.buran.common.fields.getter.IUnfieldedDataGetter;

/**
 * Buran.
 *
 * @author: ${USER} Date: 08.07.13 Time: 00:43
 */
public class FieldTarget implements IFieldTarget {
    private FieldIndex field;
    private IUnfieldedDataGetter<?> dataGetter;
    private boolean required;

    public static FieldTarget cRequired(FieldIndex field) {
        return new FieldTarget(field, FieldGetPrim.SINGLETON, true);
    }

    public static FieldTarget cOptional(FieldIndex field) {
        return new FieldTarget(field, FieldGetPrim.SINGLETON, false);
    }

    public static FieldTarget cRequired(FieldIndex fieldIndex, IUnfieldedDataGetter<?> dataGetter) {
        return new FieldTarget(fieldIndex, dataGetter, true);
    }

    public static FieldTarget cOptional(FieldIndex fieldIndex, IUnfieldedDataGetter<?> dataGetter) {
        return new FieldTarget(fieldIndex, dataGetter, false);
    }

    public FieldTarget(FieldIndex field, IUnfieldedDataGetter<?> dataGetter, boolean required) {
        this.field = field;
        this.dataGetter = dataGetter;
        this.required = required;
    }

    private FieldTarget() {
    }

    public FieldIndex getField() {
        return field;
    }

    //Falls feld nicht vorhanden und required, dann wird abgebrochen
    public boolean isRequired() {
        return required;
    }
}
