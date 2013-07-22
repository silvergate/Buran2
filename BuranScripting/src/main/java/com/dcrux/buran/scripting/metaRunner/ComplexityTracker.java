package com.dcrux.buran.scripting.metaRunner;

import com.dcrux.buran.scripting.iface.*;

import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Set;

/**
 * Buran.
 *
 * @author: ${USER} Date: 07.07.13 Time: 21:42
 */
public class ComplexityTracker implements IComplexityTracker {

    /* Memory and cpu */
    private long memoryPeak;
    private long cpuComplexity;
    private long memory;

    /* Limit */
    private final int cpuComplexityLimit;
    private final int memoryLimit;

    /* Allocated types */
    private final Set<AllocType<?>> allocTypeSet = new HashSet<AllocType<?>>();

    public ComplexityTracker(int cpuComplexityLimit, int memoryLimit) {
        this.cpuComplexityLimit = cpuComplexityLimit;
        this.memoryLimit = memoryLimit;
    }

    @Override
    public IComplexityTracker createBranch() {
        final ComplexityTracker branchedTracker =
                new ComplexityTracker(this.cpuComplexityLimit, this.memoryLimit);
        branchedTracker.memoryPeak = this.memoryPeak;
        branchedTracker.cpuComplexity = this.cpuComplexity;
        branchedTracker.memory = this.memory;
        branchedTracker.allocTypeSet.addAll(this.allocTypeSet);
        return branchedTracker;
    }

    @Override
    public void allocateMemory(int maxBytes) throws MemoryRequirementOverflow {
        this.memory += maxBytes;
        this.memoryPeak = Math.max(this.memory, this.memoryPeak);
        if (this.memoryPeak > this.memoryLimit) {
            throw new MemoryRequirementOverflow(this.memoryLimit);
        }
    }

    @Override
    public void freeMemory(int maxBytes) {
        this.memory -= maxBytes;
        if (this.memory < 0) {
            throw new IllegalArgumentException("Cannot require less than 0 bytes.");
        }
    }

    @Override
    public void allocAndFree(int maxBytes) throws MemoryRequirementOverflow {
        allocateMemory(maxBytes);
        freeMemory(maxBytes);
    }

    @Override
    public void calc(int cpuComplexity) throws CpuComplexityOverflow {
        this.cpuComplexity += cpuComplexity;
        if (this.cpuComplexity > this.cpuComplexityLimit) {
            throw new CpuComplexityOverflow(this.cpuComplexityLimit);
        }
    }

    @Override
    public <TType extends IType<?>> AllocType<TType> alloc(TType type)
            throws MemoryRequirementOverflow {
        allocateMemory(type.getMemoryMaxMemoryRequirement());
        final AllocType<TType> allocType = new AllocType<TType>(type, false);
        this.allocTypeSet.add(allocType);
        return allocType;
    }

    @Override
    public <TType extends IType<?>> AllocType<TType> allocLiteral(TType type) {
        final AllocType<TType> allocType = new AllocType<TType>(type, true);
        this.allocTypeSet.add(allocType);
        return allocType;
    }

    @Override
    public void free(AllocType<?> type) throws ProgrammErrorException {
        if (!this.allocTypeSet.contains(type)) {
            throw new ProgrammErrorException(
                    MessageFormat.format("Allocated type {0} not found", type));
        }
        if (!type.isLiteral()) {
            freeMemory(type.getType().getMemoryMaxMemoryRequirement());
        }
        this.allocTypeSet.remove(type);
    }

    public long getMemoryPeak() {
        return memoryPeak;
    }

    public long getCpuComplexity() {
        return cpuComplexity;
    }

    public int getNumberOfAllocatedTypes() {
        return this.allocTypeSet.size();
    }
}
