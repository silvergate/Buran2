package com.dcrux.buran.common.indexing.keyGen;

import com.dcrux.buran.common.indexing.mapFunction.TextFunction;

/**
 * Buran.
 *
 * @author: ${USER} Date: 09.07.13 Time: 21:33
 */
public class SingleToken implements ITokenInput {
    private final TextFunction.FuzzinessType fuzzinessType;
    private final String token;

    public SingleToken(TextFunction.FuzzinessType fuzzinessType, String token) {
        this.fuzzinessType = fuzzinessType;
        this.token = token;
    }

    public TextFunction.FuzzinessType getFuzzinessType() {
        return fuzzinessType;
    }

    public String getToken() {
        return token;
    }
}
