package com.dcrux.buran.common.fields;

import com.dcrux.buran.utils.IAltType;

/**
 * Buran.
 *
 * @author: ${USER} Date: 06.08.13 Time: 17:16
 */
public interface IFieldTarget extends IAltType<IFieldTarget> {
    static final Class<FieldIndex> TYPE_FIELD_INDEX = FieldIndex.class;
    static final Class<NodeField> TYPE_NODE_FIELD = NodeField.class;
}
