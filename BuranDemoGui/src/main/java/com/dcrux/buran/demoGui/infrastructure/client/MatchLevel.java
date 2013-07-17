package com.dcrux.buran.demoGui.infrastructure.client;

/**
 * Buran.
 *
 * @author: ${USER} Date: 15.07.13 Time: 00:04
 */
public enum MatchLevel {
    worse(0),
    ok(100),
    good(1000),
    veryGood(10000),
    perfect(100000);

    private int level;

    private MatchLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }
}
