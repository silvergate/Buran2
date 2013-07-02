package com.dcrux.buran.fields;

import java.util.HashMap;
import java.util.Map;

/**
 * Buran.
 *
 * @author: ${USER} Date: 30.06.13 Time: 23:28
 */
public class BatchGetResult {
    private Map<FieldIndex, Object> values = new HashMap<>();

    public Map<FieldIndex, Object> getValues() {
        return values;
    }
}
