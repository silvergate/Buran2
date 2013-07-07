package com.dcrux.buran.scripting.iface;

/**
 * Buran.
 *
 * @author: ${USER} Date: 07.07.13 Time: 21:42
 */
public interface IComplexityTracker {
    IComplexityTracker createBranch();

    void allocateMemory(int maxBytes) throws MemoryRequirementOverflow;

    void freeMemory(int maxBytes);

    void allocAndFree(int maxBytes) throws MemoryRequirementOverflow;

    void calc(int cpuComplexity) throws CpuComplexityOverflow;

    <TType extends IType<?>> AllocType<TType> alloc(TType type) throws MemoryRequirementOverflow;

    <TType extends IType<?>> AllocType<TType> allocLiteral(TType type);

    void free(AllocType<?> type) throws ProgrammErrorException;
}
