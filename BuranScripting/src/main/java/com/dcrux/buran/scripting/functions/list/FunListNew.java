package com.dcrux.buran.scripting.functions.list;

import com.dcrux.buran.scripting.functions.FunctionDeclaration;
import com.dcrux.buran.scripting.iface.*;
import com.dcrux.buran.scripting.iface.types.ListType;

import java.util.ArrayList;
import java.util.List;

/**
 * Buran.
 *
 * @author: ${USER} Date: 04.07.13 Time: 17:40
 */
public class FunListNew extends FunctionDeclaration<ListType> {

    private final int numberOfInputs;

    public static class Builder {
        final List<IFunctionDeclaration<?>> inputs = new ArrayList<>();

        public Builder add(IFunctionDeclaration<?> input) {
            this.inputs.add(input);
            return this;
        }

        public FunListNew get() {
            return new FunListNew(inputs);
        }
    }

    public static Builder c() {
        return new Builder();
    }

    public FunListNew(List<IFunctionDeclaration<?>> inputs) {
        for (final IFunctionDeclaration<?> input : inputs) {
            addInput(input);
        }
        this.numberOfInputs = inputs.size();
    }

    public int getNumberOfInputs() {
        return numberOfInputs;
    }

    @Override
    public AllocType<ListType> getMeta(ITypeState state) throws ProgrammErrorException {
        state.getCompTracker().calc(1);

        final List<AllocType<?>> allocTypes = new ArrayList<>();
        final List<IType<?>> types = new ArrayList<>();

        final int numOfInputs = getInput().size();
        for (int i = 0; i < numOfInputs; i++) {
            final AllocType<? extends IType> inputMeta = getInput(i).getMeta(state);
            allocTypes.add(inputMeta);
            types.add(inputMeta.getType());
        }

        final AllocType<ListType> allocTypeRet = state.getCompTracker().alloc(new ListType(types));

        /* Free inputs */
        for (final AllocType<?> allocType : allocTypes) {
            state.getCompTracker().free(allocType);
        }

        return allocTypeRet;
    }
}
