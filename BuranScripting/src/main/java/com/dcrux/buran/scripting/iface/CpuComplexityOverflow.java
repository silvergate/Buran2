package com.dcrux.buran.scripting.iface;

import java.text.MessageFormat;

/**
 * Buran.
 *
 * @author: ${USER} Date: 07.07.13 Time: 21:28
 */
public class CpuComplexityOverflow extends ProgrammErrorException {

    private final int limit;

    public CpuComplexityOverflow(int limit) {
        super(MessageFormat.format("CPU-limit per branch is set to {0}.", limit));
        this.limit = limit;
    }

    public int getLimit() {
        return limit;
    }
}
