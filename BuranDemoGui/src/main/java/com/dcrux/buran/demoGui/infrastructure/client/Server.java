package com.dcrux.buran.demoGui.infrastructure.client;

/**
 * Buran.
 *
 * @author: ${USER} Date: 17.07.13 Time: 23:51
 */
public class Server {
    public static BuranServiceAsync get() {
        return BuranService.App.getInstance();
    }
}
