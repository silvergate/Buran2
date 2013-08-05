package com.dcrux.buran.common.indexing.keyGen;

/**
 * Buran.
 *
 * @author: ${USER} Date: 06.08.13 Time: 00:51
 */
public class Tokens implements IKeyGen {
    private String tokens;

    public Tokens(String tokens) {
        this.tokens = tokens;
    }

    public static Tokens c(String tokens) {
        return new Tokens(tokens);
    }

    private Tokens() {
    }

    public String getTokens() {
        return tokens;
    }
}
