package com.dcrux.buran.demoGui.plugins.listview.client;

import com.dcrux.buran.common.UserId;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.UListElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Buran.
 *
 * @author: ${USER} Date: 21.07.13 Time: 21:02
 */
public class ViewList {
    private Widget rootElement;
    private DescModule descModule = new DescModule(new UserId(0));

    public Widget getRootElement() {
        return rootElement;
    }

    interface ViewListUiBinder extends UiBinder<HTMLPanel, ViewList> {
    }

    private static ViewListUiBinder ourUiBinder = GWT.create(ViewListUiBinder.class);
    @UiField
    UListElement thumbsList;

    public ViewList() {
        rootElement = ourUiBinder.createAndBindUi(this);
    }
}