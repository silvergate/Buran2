package com.dcrux.buran.demoGui.plugins.fileApp.client;

import com.dcrux.buran.demoGui.infrastructure.client.*;
import com.dcrux.buran.demoGui.plugins.publicTypes.ResultMainApp;
import com.dcrux.buran.demoGui.plugins.publicTypes.TypeMainApp;
import com.google.gwt.user.client.ui.Widget;

/**
 * Buran.
 *
 * @author: ${USER} Date: 15.07.13 Time: 00:12
 */
public class ChatAppPlugin implements PluginIntent<VoidQuery, ResultMainApp> {
    private Widget singletonApp;
    private ResultMainApp singletonResultMainApp;
    private final static IntentRef REF =
            new IntentRef("com.dcrux.buran.demoGui.plugins.chatApp" + ".client.ChatAppPlugin");

    @Override
    public Description getDescription() {
        return new Description("Files");
    }

    @Override
    public Description getDescription(VoidQuery query) {
        return new Description("Files");
    }

    @Override
    public IntentType<VoidQuery, ResultMainApp> getRef() {
        return TypeMainApp.SINGLETON;
    }

    @Override
    public ResultMainApp run(VoidQuery query) {
        if (singletonApp == null) {
            FileMainUi fileMainUi = new FileMainUi();
            singletonApp = fileMainUi.getRootElement();
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
