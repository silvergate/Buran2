package com.dcrux.buran.common.fields;

import com.dcrux.buran.utils.IAltType;

/**
 * Buran.
 *
 * @author: ${USER} Date: 06.08.13 Time: 17:16
 */
public interface IFieldTarget extends IAltType<IFieldTarget> {
    static final Class<FieldTarget> TYPE_FIELD_TARGET = FieldTarget.class;
    static final Class<NodeFieldTarget> TYPE_NODE_FIELD = NodeFieldTarget.class;
}
