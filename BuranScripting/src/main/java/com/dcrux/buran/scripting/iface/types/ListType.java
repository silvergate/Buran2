package com.dcrux.buran.scripting.iface.types;

import com.dcrux.buran.scripting.iface.IType;

import java.util.List;

/**
 * Buran.
 *
 * @author: ${USER} Date: 05.07.13 Time: 08:41
 */
public class ListType implements IType<Object[]> {

    public ListType(List<IType<?>> types) {
        this.types = types;
    }

    private List<IType<?>> types;

    public List<IType<?>> getTypes() {
        return types;
    }

    @Override
    public IType<Object[]> combineWith(IType<?> other) {
        final ListType otherCast = (ListType) other;
        if (otherCast.types.equals(this.types)) {
            return this;
        }
        throw new IllegalArgumentException(
                "Not yet implemented (2 list types sind nicht " + "kombinierbar");
    }

    @Override
    public int getMemoryMaxMemoryRequirement() {
        int max = 0;
        for (final IType<?> type : this.types) {
            max = Math.max(type.getMemoryMaxMemoryRequirement(), max);
        }
        return max;
    }
}
