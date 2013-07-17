package com.dcrux.buran.common.indexing.mapInput;

import com.dcrux.buran.common.fields.FieldIndex;

/**
 * Buran.
 *
 * @author: ${USER} Date: 08.07.13 Time: 00:43
 */
public class FieldTarget implements IFieldTarget {
    private FieldIndex field;
    private boolean required;

    public FieldTarget(FieldIndex field, boolean required) {
        this.field = field;
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
