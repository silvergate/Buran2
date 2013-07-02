package com.dcrux.buran.refimpl.baseModules.fields;

import com.dcrux.buran.common.fields.FieldIndex;

/**
 * Buran.
 *
 * @author: ${USER} Date: 29.06.13 Time: 15:01
 */
//TODO: Raus in die wrapper-klassen
@Deprecated
public class DocFields {
    /* Class prefixes */
    public static final String NODE_CLASS_PREFIX = "node";

    /* Incubation Node */
    public static final String INC_FIELD_ADD_TIME = "added";
    public static final String INC_FIELD_SENDER = "sender";
    public static final String INC_FIELD_UPDATE_VERSION = "uversion";
    public static final String INC_FIELD_UPDATE_NODE = "unode";

    /* Live node */
    public static final String FIELD_FIRST_COMMIT_TIME = "fct";
    public static final String FIELD_COMMIT_TIME = "ct";
    public static final String FIELD_SENDER = "sender";
    public static final String FIELD_VERSION = "version";

    /* Common */
    public static final String FIELD_COMMON_LIVE = "live";
    public static final String FIELD_COMMON_LABELS = "labels";
    public static final String FIELD_COMMON_CLASS_LABELS = "clabels";

    /* Class relation */
    public static final String CR_FIELD_LIVE = "live";

    public static String fieldName(FieldIndex index) {
        return "f" + index.getIndex();
    }
}
