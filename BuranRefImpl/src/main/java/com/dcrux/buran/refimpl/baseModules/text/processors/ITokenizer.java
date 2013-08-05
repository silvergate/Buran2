package com.dcrux.buran.refimpl.baseModules.text.processors;

/**
 * Buran.
 *
 * @author: ${USER} Date: 05.08.13 Time: 12:28
 */
public interface ITokenizer {
    public interface ICallback {
        void nextToken(String token, boolean lastToken);

        void nextCdata(int typeInt, String cdata);
    }

    void tokenize(String input, ICallback callback);

}
