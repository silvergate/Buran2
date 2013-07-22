package com.dcrux.buran.demoGui.plugins.listview.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.HTMLPanel;

/**
 * Buran.
 *
 * @author: ${USER} Date: 21.07.13 Time: 20:21
 */
public class ViewItem {
    interface ViewItemUiBinder extends UiBinder<HTMLPanel, ViewItem> {
    }

    private static ViewItemUiBinder ourUiBinder = GWT.create(ViewItemUiBinder.class);

    public ViewItem() {
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);

    }
}