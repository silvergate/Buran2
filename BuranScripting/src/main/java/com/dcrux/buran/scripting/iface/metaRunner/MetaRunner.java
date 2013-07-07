package com.dcrux.buran.scripting.iface.metaRunner;

import com.dcrux.buran.scripting.iface.*;
import com.dcrux.buran.scripting.iface.types.VoidType;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Buran.
 *
 * @author: ${USER} Date: 05.07.13 Time: 08:14
 */
public class MetaRunner {

    private final int cpuComplexityLimit;
    private final int memoryLimit;

    public MetaRunner(int cpuComplexityLimit, int memoryLimit) {
        this.cpuComplexityLimit = cpuComplexityLimit;
        this.memoryLimit = memoryLimit;
    }

    public IType<?> evaluate(Block block) throws ProgrammErrorException {
        final ComplexityTracker complexityTracker =
                new ComplexityTracker(this.cpuComplexityLimit, this.memoryLimit);
        final TypeState state = new TypeState(complexityTracker);
        Set<IType> retTypes = new HashSet<>();
        Collection<TypeState> brachedStates = new HashSet<>();
        brachedStates.add(state);
        evaluateInt(block, state, 0, retTypes, brachedStates);
        Set<IType<?>> types = new HashSet<>();
        for (IType functionMeta : retTypes) {
            types.add(functionMeta);
        }

        long memPeak = 0;
        long cpuPeak = 0;
        for (final TypeState processedState : brachedStates) {
             /* Check for not freed types */
            if (processedState.getComplexityTracker().getNumberOfAllocatedTypes() != 0) {
                throw new ProgrammErrorException(MessageFormat.format("There are still {0} " +
                        "allocated types. Check functions declarations.",
                        complexityTracker.getNumberOfAllocatedTypes()));
            }
            memPeak = Math.max(memPeak, processedState.getComplexityTracker().getMemoryPeak());
            cpuPeak = Math.max(cpuPeak, processedState.getComplexityTracker().getCpuComplexity());
        }

        System.out.println(MessageFormat.format("Mem-Peak: {0}, CPU Peak: {1}", memPeak, cpuPeak));
        return TypeCombiner.combine(types);
    }

    private void evaluateInt(Block block, TypeState state, int startLine, Set<IType> outRet,
            Collection<TypeState> outBranchedStates) throws ProgrammErrorException {
        for (int line = startLine; line < block.getNumberOfLines(); line++) {
            final IFunctionDeclaration<?> func = block.getLine(line);
            final AllocType<? extends IType> meta = func.getMeta(state);
             /* Deallocate return value */
            state.getCompTracker().free(meta);
            if (state.getRetType() != null) {
                outRet.add(state.getRetType());
                return;
            }
            if (state.getJumpTo() != null) {
                line = state.getJumpTo().getNum() - 1;
                state.clearJumpTo();
                continue;
            }
            if (state.getBranchTo() != null) {
                 /* Branching */
                final TypeState branchedState = state.getStateForBranch();
                outBranchedStates.add(branchedState);
                evaluateInt(block, branchedState, state.getBranchTo().getNum(), outRet,
                        outBranchedStates);
                state.continueAfterBranch();
            }
        }
        outRet.add(VoidType.SINGLETON);
    }

}
