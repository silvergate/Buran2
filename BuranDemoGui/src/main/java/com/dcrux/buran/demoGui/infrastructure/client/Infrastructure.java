package com.dcrux.buran.demoGui.infrastructure.client;

import com.dcrux.buran.demoGui.plugins.fileApp.client.ChatAppPlugin;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Document;

/**
 * Buran.
 *
 * @author: ${USER} Date: 14.07.13 Time: 23:13
 */
public class Infrastructure implements EntryPoint {

    private void register() {
        IntentRegistry.SINGLETON.register(new ChatAppPlugin());
    }

    public void onModuleLoad() {
        register();
        MainPage mainPage = new MainPage();
        Document.get().getBody().appendChild(mainPage.getRootElement().getElement());
        //loadSidebar();
    }
}
