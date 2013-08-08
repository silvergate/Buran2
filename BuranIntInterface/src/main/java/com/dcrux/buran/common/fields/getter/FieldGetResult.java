package com.dcrux.buran.common.fields.getter;

import com.dcrux.buran.common.fields.IFieldTarget;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Buran.
 *
 * @author: ${USER} Date: 30.06.13 Time: 23:28
 */
public class FieldGetResult implements Serializable {
    private Map<IFieldTarget, Serializable> values = new HashMap<IFieldTarget, Serializable>();

    public Map<IFieldTarget, Serializable> getValues() {
        return values;
    }
}
