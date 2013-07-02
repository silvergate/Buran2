package com.dcrux.buran.fields;

import com.dcrux.buran.fields.getter.IDataGetter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Buran.
 *
 * @author: ${USER} Date: 01.07.13 Time: 00:47
 */
public class BatchGet implements Serializable {

    public static BatchGet c(FieldIndex index, IDataGetter<?> getter) {
        final BatchGet batchGet = new BatchGet();
        return batchGet.add(index, getter);
    }

    private final Map<FieldIndex, IDataGetter<?>> entries = new HashMap<>();

    public Map<FieldIndex, IDataGetter<?>> getEntries() {
        return entries;
    }

    public BatchGet add(FieldIndex index, IDataGetter<?> getter) {
        this.entries.put(index, getter);
        return this;
    }
}
