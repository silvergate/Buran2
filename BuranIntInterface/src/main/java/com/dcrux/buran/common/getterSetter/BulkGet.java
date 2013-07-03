package com.dcrux.buran.common.getterSetter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Buran.
 *
 * @author: ${USER} Date: 03.07.13 Time: 02:18
 */
public class BulkGet implements IDataGetter<IBulkGetResult> {
    private final List<IDataGetter<?>> dataGetterList = new ArrayList<>();

    public <TRetval extends Serializable> BulkGetIndex<TRetval> add(
            IDataGetter<TRetval> dataGetter) {
        this.dataGetterList.add(dataGetter);
        return new BulkGetIndex<TRetval>(this.dataGetterList.size() - 1);
    }

    public List<IDataGetter<?>> getDataGetterList() {
        return dataGetterList;
    }
}
