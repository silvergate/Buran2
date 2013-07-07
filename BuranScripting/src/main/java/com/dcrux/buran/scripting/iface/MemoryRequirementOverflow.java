package com.dcrux.buran.scripting.iface;

/**
 * Buran.
 *
 * @author: ${USER} Date: 07.07.13 Time: 21:28
 */
public class MemoryRequirementOverflow extends ProgrammErrorException {
    private final int limit;

    public MemoryRequirementOverflow(int limit) {
        super("");
        this.limit = limit;
    }

    public int getLimit() {
        return limit;
    }
}
