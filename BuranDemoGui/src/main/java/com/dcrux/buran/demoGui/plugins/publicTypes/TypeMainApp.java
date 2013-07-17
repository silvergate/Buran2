package com.dcrux.buran.demoGui.plugins.publicTypes;

import com.dcrux.buran.demoGui.infrastructure.client.IntentType;

/**
 * Buran.
 *
 * @author: ${USER} Date: 15.07.13 Time: 00:14
 */
public class TypeMainApp<VoidQuery, ResultMainApp> extends IntentType {
    public static final TypeMainApp SINGLETON = new TypeMainApp();

    public TypeMainApp() {
        super("com.mainApp");
    }
}
