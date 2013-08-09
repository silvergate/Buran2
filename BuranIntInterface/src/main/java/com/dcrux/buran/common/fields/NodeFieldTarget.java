package com.dcrux.buran.common.fields;

/**
 * Buran.
 *
 * @author: ${USER} Date: 06.08.13 Time: 17:07
 */
public enum NodeFieldTarget implements IFieldTarget {
    sender,
    created,
    lastUpdated,
    domains,
    classes;

    @Override
    public Class<? extends IFieldTarget> getType() {
        return NodeFieldTarget.class;
    }

    @Override
    public <TType extends IFieldTarget> TType get(Class<TType> type) {
        return (TType) this;
    }

    @Override
    public boolean is(Class<? extends IFieldTarget> type) {
        return type.equals(NodeFieldTarget.class);
    }
}
