package com.dcrux.buran.common.fields.getter;

import com.dcrux.buran.common.fields.FieldIndex;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Buran.
 *
 * @author: ${USER} Date: 30.06.13 Time: 23:28
 */
public class FieldGetResult implements Serializable {
    private Map<FieldIndex, Serializable> values = new HashMap<FieldIndex, Serializable>();

    public Map<FieldIndex, Serializable> getValues() {
        return values;
    }
}
