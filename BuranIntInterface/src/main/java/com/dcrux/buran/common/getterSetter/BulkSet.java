package com.dcrux.buran.common.getterSetter;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Buran.
 *
 * @author: ${USER} Date: 03.07.13 Time: 02:58
 */
public class BulkSet implements IDataSetter {
    private final Collection<IDataSetter> dataSetterSet = new ArrayList<>();

    public static BulkSet c(IDataSetter setter) {
        final BulkSet bulkSet = new BulkSet();
        bulkSet.add(setter);
        return bulkSet;
    }

    public BulkSet add(IDataSetter setter) {
        this.dataSetterSet.add(setter);
        return this;
    }

    //TODO: Rename, is not a SET, its a collection
    public Collection<IDataSetter> getDataSetterSet() {
        return dataSetterSet;
    }
}
