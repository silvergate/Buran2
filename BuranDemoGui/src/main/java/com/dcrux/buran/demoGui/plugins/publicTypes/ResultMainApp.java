package com.dcrux.buran.demoGui.plugins.publicTypes;

import com.google.gwt.user.client.ui.Widget;

import java.io.Serializable;

/**
 * Buran.
 *
 * @author: ${USER} Date: 15.07.13 Time: 00:20
 */
public class ResultMainApp implements Serializable {
    private Widget widget;

    public ResultMainApp(Widget widget) {
        this.widget = widget;
    }

    public Widget get() {
        return this.widget;
    }
}
