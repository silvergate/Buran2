package com.dcrux.buran.refimpl.baseModules.text.processors;

import java.util.List;

/**
 * Buran.
 *
 * @author: ${USER} Date: 05.08.13 Time: 12:36
 */
public interface IEmmitter {
    public static interface ICallback {
        void emit(byte[] data);
    }

    void begin();

    void nextToken(String token, ICallback callback, boolean lastToken);

    KeyRange singleToken(String token);

    KeyRange multiToken(List<String> tokens);
}
