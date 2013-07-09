package com.dcrux.buran.indexing.keyGen;

import com.dcrux.buran.indexing.mapFunction.TextFunction;

/**
 * Buran.
 *
 * @author: ${USER} Date: 09.07.13 Time: 21:33
 */
public class MultiToken implements ITokenInput {
    private final TextFunction.FuzzinessType fuzzinessType;
    private final String stringWithTokens;
    private final TextFunction.ProximityType proximity;

    public MultiToken(TextFunction.FuzzinessType fuzzinessType, String stringWithTokens,
            TextFunction.ProximityType proximity) {
        this.fuzzinessType = fuzzinessType;
        this.stringWithTokens = stringWithTokens;
        this.proximity = proximity;
    }

    public TextFunction.FuzzinessType getFuzzinessType() {
        return fuzzinessType;
    }

    public String getStringWithTokens() {
        return stringWithTokens;
    }

    public TextFunction.ProximityType getProximity() {
        return proximity;
    }
}
