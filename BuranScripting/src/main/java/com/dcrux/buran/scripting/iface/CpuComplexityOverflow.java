package com.dcrux.buran.scripting.iface;


/**
 * Buran.
 *
 * @author: ${USER} Date: 07.07.13 Time: 21:28
 */
public class CpuComplexityOverflow extends ProgrammErrorException {

    private int limit;

    public CpuComplexityOverflow(int limit) {
        super("CPU-limit per branch is set to " + limit);
        this.limit = limit;
    }

    private CpuComplexityOverflow() {
    }

    public int getLimit() {
        return limit;
    }
}
