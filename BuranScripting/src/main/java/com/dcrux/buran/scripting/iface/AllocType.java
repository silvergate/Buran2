package com.dcrux.buran.scripting.iface;

/**
 * Buran.
 *
 * @author: ${USER} Date: 07.07.13 Time: 22:53
 */
public class AllocType<TType extends IType> {
    private final TType type;
    private final boolean isLiteral;

    public AllocType(TType type, boolean literal) {
        this.type = type;
        isLiteral = literal;
    }

    @Override
    public String toString() {
        return "AllocType{" +
                "type=" + type +
                ", isLiteral=" + isLiteral +
                '}';
    }

    public TType getType() {
        return type;
    }

    public boolean isLiteral() {
        return isLiteral;
    }
}
