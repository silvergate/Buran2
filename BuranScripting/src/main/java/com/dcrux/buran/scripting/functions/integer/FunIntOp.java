package com.dcrux.buran.scripting.functions.integer;

import com.dcrux.buran.scripting.functions.FunctionDeclaration;
import com.dcrux.buran.scripting.iface.AllocType;
import com.dcrux.buran.scripting.iface.IFunctionDeclaration;
import com.dcrux.buran.scripting.iface.ITypeState;
import com.dcrux.buran.scripting.iface.ProgrammErrorException;
import com.dcrux.buran.scripting.iface.types.IntegerType;

/**
 * Buran.
 *
 * @author: ${USER} Date: 04.07.13 Time: 17:40
 */
public class FunIntOp extends FunctionDeclaration<IntegerType> {

    public static enum OpType {
        add,
        sub,
        mul,
        div,
        mod
    }

    private final OpType op;

    public static FunIntOp add(IFunctionDeclaration<IntegerType> i1,
            IFunctionDeclaration<IntegerType> i2) {
        return new FunIntOp(OpType.add, i1, i2);
    }

    public static FunIntOp sub(IFunctionDeclaration<IntegerType> i1,
            IFunctionDeclaration<IntegerType> i2) {
        return new FunIntOp(OpType.sub, i1, i2);
    }

    public FunIntOp(OpType operation, IFunctionDeclaration<IntegerType> i1,
            IFunctionDeclaration<IntegerType> i2) {
        this.op = operation;
        addInput(i1);
        addInput(i2);
    }

    public OpType getOp() {
        return op;
    }


    static boolean safeAdd(long left, long right) {
        if (right > 0 ? left > Long.MAX_VALUE - right : left < Long.MIN_VALUE - right) {
            return false;
        }
        return true;
    }

    static final boolean safeSubtract(long left, long right) {
        if (right > 0 ? left < Long.MIN_VALUE + right : left > Long.MAX_VALUE + right) {
            return false;
        }
        return true;
    }

    static final boolean safeMultiply(long left, long right) {
        if (right > 0 ? left > Long.MAX_VALUE / right || left < Long.MIN_VALUE / right :
                (right < -1 ? left > Long.MIN_VALUE / right || left < Long.MAX_VALUE / right :
                        right == -1 && left == Long.MIN_VALUE)) {
            return false;
        }
        return true;
    }

    static final boolean safeDivide(long left, long right) {
        if ((left == Long.MIN_VALUE) && (right == -1)) {
            return false;
        }
        return true;
    }

    @Override
    public AllocType<IntegerType> getMeta(ITypeState state) throws ProgrammErrorException {
        final AllocType<IntegerType> iF1 = getInput(0, IntegerType.class).getMeta(state);
        final AllocType<IntegerType> iF2 = getInput(1, IntegerType.class).getMeta(state);
        final IntegerType it1 = iF1.getType();
        final IntegerType it2 = iF2.getType();

        Long min = null;
        Long max = null;

        switch (this.op) {
            case add:
                final boolean minSafe = safeAdd(it1.getMinValue(), it2.getMinValue());
                final boolean maxSafe = safeAdd(it1.getMinValue(), it2.getMinValue());
                if (minSafe && maxSafe) {
                    min = it1.getMinValue() + it2.getMinValue();
                    max = it1.getMaxValue() + it2.getMaxValue();
                }
                break;
            case sub:
                final boolean minSafeS = safeSubtract(it1.getMinValue(), it2.getMinValue());
                final boolean maxSafeS = safeSubtract(it1.getMinValue(), it2.getMinValue());
                if (minSafeS && maxSafeS) {
                    min = it1.getMinValue() - it2.getMinValue();
                    max = it1.getMaxValue() - it2.getMaxValue();
                }
                break;
            case mul:
                final boolean minSafeM = safeMultiply(it1.getMinValue(), it2.getMinValue());
                final boolean maxSafeM = safeMultiply(it1.getMinValue(), it2.getMinValue());
                if (minSafeM && maxSafeM) {
                    min = it1.getMinValue() * it2.getMinValue();
                    max = it1.getMaxValue() * it2.getMaxValue();
                }
                break;
            case div:
                if (it2.intersect(new IntegerType(0, 0))) {
                    throw new ProgrammErrorException("Second argument of integer division " +
                            "operation can contain zero. Possible div by zero.");
                }
                final boolean minSafeD = safeDivide(it1.getMinValue(), it2.getMinValue());
                final boolean maxSafeD = safeDivide(it1.getMinValue(), it2.getMinValue());
                if (minSafeD && maxSafeD) {
                    min = it1.getMinValue() / it2.getMinValue();
                    max = it1.getMaxValue() / it2.getMaxValue();
                }
                break;
            case mod:
                min = 0l;
                max = it2.getMaxValue() - 1;
                break;
            default:
                throw new IllegalArgumentException("Unknown operation");
        }

        if (min == null) {
            min = Long.MIN_VALUE;
        }
        if (max == null) {
            max = Long.MAX_VALUE;
        }

        final AllocType<IntegerType> allocTypeRet =
                state.getCompTracker().alloc(new IntegerType(min, max));
        state.getCompTracker().calc(8);
        state.getCompTracker().free(iF1);
        state.getCompTracker().free(iF2);

        return allocTypeRet;
    }
}
