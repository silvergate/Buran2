package com.dcrux.buran.common.query;

import com.dcrux.buran.common.fields.FieldIndex;
import com.dcrux.buran.common.fields.NodeFieldTarget;
import com.dcrux.buran.utils.WrappedAltType;

import java.io.Serializable;

/**
 * Buran.
 *
 * @author: ${USER} Date: 07.08.13 Time: 10:42
 */
public class IndexFieldTarget extends WrappedAltType<Serializable> {

    public static final Class<FieldIndex> TYPE_FIELD_INDEX = FieldIndex.class;
    public static final Class<NodeFieldTarget> TYPE_NODE_FIELD = NodeFieldTarget.class;

    public static IndexFieldTarget index(FieldIndex fieldIndex) {
        return new IndexFieldTarget(fieldIndex);
    }

    public static IndexFieldTarget node(NodeFieldTarget nodeFieldTarget) {
        return new IndexFieldTarget(nodeFieldTarget);
    }

    private IndexFieldTarget(Serializable data) {
        super(data);
    }
}
