package com.dcrux.buran.common.fields.getter;

/**
 * Buran.
 *
 * @author: ${USER} Date: 30.06.13 Time: 13:10
 */
public class GetPrim implements IUnfieldedDataGetter<String> {
    public static final GetPrim SINGLETON = new GetPrim();

    private GetPrim() {
    }
}
