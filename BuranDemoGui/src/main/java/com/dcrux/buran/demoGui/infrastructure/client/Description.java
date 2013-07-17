package com.dcrux.buran.demoGui.infrastructure.client;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Buran.
 *
 * @author: ${USER} Date: 14.07.13 Time: 23:54
 */
public class Description implements IsSerializable {
    private String shortStr;
    private String longStr;

    public Description(String shortStr, String longStr) {
        this.shortStr = shortStr;
        this.longStr = longStr;
    }

    public String getShortStr() {
        return shortStr;
    }

    public String getLongStr() {
        return longStr;
    }

    private Description() {
    }

    public Description(String shortStr) {
        this.shortStr = shortStr;
        this.longStr = shortStr;
    }
}
