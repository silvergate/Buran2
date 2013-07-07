package com.dcrux.buran.scripting.functions.integer;

import com.dcrux.buran.scripting.functions.FunctionDeclaration;
import com.dcrux.buran.scripting.iface.AllocType;
import com.dcrux.buran.scripting.iface.IFunctionDeclaration;
import com.dcrux.buran.scripting.iface.ITypeState;
import com.dcrux.buran.scripting.iface.ProgrammErrorException;
import com.dcrux.buran.scripting.iface.types.BoolType;
import com.dcrux.buran.scripting.iface.types.IntegerType;

/**
 * Buran.
 *
 * @author: ${USER} Date: 04.07.13 Time: 17:40
 */
public class FunIntCmp extends FunctionDeclaration<BoolType> {

    public static enum CmpOp {
        equal,
        greater,
        lesser
    }

    private final CmpOp op;

    public static FunIntCmp eq(IFunctionDeclaration<IntegerType> i1,
            IFunctionDeclaration<IntegerType> i2) {
        return new FunIntCmp(CmpOp.equal, i1, i2);
    }

    public static FunIntCmp gt(IFunctionDeclaration<IntegerType> i1,
            IFunctionDeclaration<IntegerType> i2) {
        return new FunIntCmp(CmpOp.greater, i1, i2);
    }

    public static FunIntCmp le(IFunctionDeclaration<IntegerType> i1,
            IFunctionDeclaration<IntegerType> i2) {
        return new FunIntCmp(CmpOp.lesser, i1, i2);
    }

    public FunIntCmp(CmpOp operation, IFunctionDeclaration<IntegerType> i1,
            IFunctionDeclaration<IntegerType> i2) {
        this.op = operation;
        addInput(i1);
        addInput(i2);
    }

    public CmpOp getOp() {
        return op;
    }

    @Override
    public AllocType<BoolType> getMeta(ITypeState state) throws ProgrammErrorException {
        final AllocType<IntegerType> itF1 = getInput(0, IntegerType.class).getMeta(state);
        final AllocType<IntegerType> itF2 = getInput(1, IntegerType.class).getMeta(state);
        final IntegerType it1 = itF1.getType();
        final IntegerType it2 = itF2.getType();

        BoolType.BoolRange range;
        switch (op) {
            case equal:
                if (it1.equals(it2)) {
                    range = BoolType.BoolRange.trueOnly;
                } else {
                    if (!it1.intersect(it2)) {
                        range = BoolType.BoolRange.falseOnly;
                    } else {
                        range = BoolType.BoolRange.trueOrFalse;
                    }
                }
                break;
            case greater:
                if (it1.equals(it2)) {
                    range = BoolType.BoolRange.falseOnly;
                } else {
                    if (it1.isStrictlyGreaterThan(it2)) {
                        range = BoolType.BoolRange.trueOnly;
                    } else if (it2.isStrictlyGreaterThan(it1)) {
                        range = BoolType.BoolRange.falseOnly;
                    } else {
                        range = BoolType.BoolRange.trueOrFalse;
                    }
                }
                break;
            case lesser:
                if (it1.equals(it2)) {
                    range = BoolType.BoolRange.falseOnly;
                } else {
                    if (it1.isStrictlyLesserThan(it2)) {
                        range = BoolType.BoolRange.trueOnly;
                    } else if (it2.isStrictlyLesserThan(it1)) {
                        range = BoolType.BoolRange.falseOnly;
                    } else {
                        range = BoolType.BoolRange.trueOrFalse;
                    }
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown operation");
        }
        state.getCompTracker().calc(4);
        final BoolType retType = new BoolType(range);
        final AllocType<BoolType> allocRet = state.getCompTracker().alloc(retType);
        state.getCompTracker().free(itF1);
        state.getCompTracker().free(itF2);
        return allocRet;
    }
}
