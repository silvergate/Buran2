package com.dcrux.buran.demoGui.infrastructure.client;

import com.dcrux.buran.demoGui.plugins.publicTypes.ResultMainApp;
import com.dcrux.buran.demoGui.plugins.publicTypes.TypeMainApp;
import com.github.gwtbootstrap.client.ui.base.ListItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.UListElement;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Widget;

import java.util.Collection;


/**
 * Buran.
 *
 * @author: ${USER} Date: 15.07.13 Time: 09:38
 */
public class MainPage implements ValueChangeHandler<String> {
    @Override
    public void onValueChange(ValueChangeEvent<String> event) {
        final String newRef = event.getValue();
        loadToMainScreen(newRef);
    }

    interface MainPageUiBinder extends UiBinder<HTMLPanel, MainPage> {
    }

    @UiField
    UListElement sidebarLinks;

    @UiField
    DivElement mainContent;

    private HTMLPanel rootElement;

    private static MainPageUiBinder ourUiBinder = GWT.create(MainPageUiBinder.class);

    private void loadSidebar() {
        final Collection<PluginIntent<VoidQuery, ResultMainApp>> results =
                IntentRegistry.SINGLETON.getAll(TypeMainApp.SINGLETON, VoidQuery.SINGLETON);
        for (final PluginIntent<VoidQuery, ResultMainApp> result : results) {
            final Hyperlink hl = new Hyperlink(result.getDescription().getShortStr(),
                    result.getRefReal().getRef());
            final ListItem li = new ListItem(hl);
            this.sidebarLinks.appendChild(li.getElement());
        }
    }

    private void loadToMainScreen(String ref) {
        final PluginIntent result = IntentRegistry.SINGLETON.get(new IntentRef(ref));
        if (result != null) {
            ResultMainApp resultMainApp = (ResultMainApp) result.run(VoidQuery.SINGLETON);
            loadToMainScreen(resultMainApp.get());
        }
    }

    private void loadToMainScreen(Widget widget) {
        while (this.mainContent.hasChildNodes()) {
            this.mainContent.removeChild(this.mainContent.getLastChild());
        }

        this.mainContent.appendChild(widget.getElement());
    }

    public MainPage() {
        this.rootElement = ourUiBinder.createAndBindUi(this);
        History.addValueChangeHandler(this);
        loadSidebar();
    }

    public HTMLPanel getRootElement() {
        return rootElement;
    }
}