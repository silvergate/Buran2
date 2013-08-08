package com.dcrux.buran.query;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Buran.
 *
 * @author: ${USER} Date: 06.08.13 Time: 22:22
 */
public class SingleIndexDef implements Serializable {
    private boolean forNotificationOnly = false;
    private Map<IndexedFieldId, IndexedFieldDef> fieldDef =
            new HashMap<IndexedFieldId, IndexedFieldDef>();

    public Map<IndexedFieldId, IndexedFieldDef> getFieldDef() {
        return fieldDef;
    }

    public boolean isForNotificationOnly() {
        return forNotificationOnly;
    }
}
