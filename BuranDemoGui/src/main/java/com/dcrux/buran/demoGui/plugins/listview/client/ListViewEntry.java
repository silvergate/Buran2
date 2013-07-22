package com.dcrux.buran.demoGui.plugins.listview.client;

import com.dcrux.buran.demoGui.infrastructure.client.*;
import com.dcrux.buran.demoGui.plugins.publicTypes.ResultMainApp;
import com.dcrux.buran.demoGui.plugins.publicTypes.TypeMainApp;
import com.google.gwt.user.client.ui.Widget;

/**
 * Buran.
 *
 * @author: ${USER} Date: 22.07.13 Time: 01:19
 */
public class ListViewEntry implements PluginIntent<VoidQuery, ResultMainApp> {
    private Widget singletonApp;
    private ResultMainApp singletonResultMainApp;
    private final static IntentRef REF =
            new IntentRef("com.dcrux.buran.demoGui.plugins.itemsApp" + ".client.Items");

    @Override
    public Description getDescription() {
        return new Description("Items");
    }

    @Override
    public Description getDescription(VoidQuery query) {
        return new Description("Items");
    }

    @Override
    public IntentType<VoidQuery, ResultMainApp> getRef() {
        return TypeMainApp.SINGLETON;
    }

    @Override
    public ResultMainApp run(VoidQuery query) {
        if (singletonApp == null) {
            ViewList fileUploadUi = new ViewList();
            singletonApp = fileUploadUi.getRootElement();
            this.singletonResultMainApp = new ResultMainApp(this.singletonApp);
        }
        return this.singletonResultMainApp;
    }

    @Override
    public MatchLevel getMatchLevel(VoidQuery query) {
        return MatchLevel.veryGood;
    }

    @Override
    public IntentRef<VoidQuery, ResultMainApp> getRefReal() {
        return REF;
    }
}
