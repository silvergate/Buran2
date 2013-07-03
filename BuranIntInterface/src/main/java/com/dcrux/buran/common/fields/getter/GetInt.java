package com.dcrux.buran.common.fields.getter;

/**
 * Buran.
 *
 * @author: ${USER} Date: 30.06.13 Time: 13:10
 */
public class GetInt implements IUnfieldedDataGetter<Number> {
    public static final GetInt SINGLETON = new GetInt();

    private GetInt() {
    }
}
