package com.dcrux.buran.common.fields.getter;

/**
 * Buran.
 *
 * @author: ${USER} Date: 30.06.13 Time: 13:10
 */
public class GetStr implements IUnfieldedDataGetter<String> {
    public static final GetStr SINGLETON = new GetStr();

    private GetStr() {
    }
}
