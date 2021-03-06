package com.dcrux.buran.demoGui.infrastructure.client;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Buran.
 *
 * @author: ${USER} Date: 14.07.13 Time: 23:57
 */
public class IntentType<TIntentQuery, TResult> implements IsSerializable {
    private String ref;

    public IntentType(String ref) {
        this.ref = ref;
    }

    private IntentType() {
    }

    public String getRef() {
        return ref;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IntentType that = (IntentType) o;

        if (!ref.equals(that.ref)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return ref.hashCode();
    }
}
