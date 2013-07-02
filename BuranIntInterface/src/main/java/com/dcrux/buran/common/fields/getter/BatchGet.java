package com.dcrux.buran.common.fields.getter;

import com.dcrux.buran.common.fields.FieldIndex;
import com.dcrux.buran.common.fields.IDataGetter;

import java.util.HashMap;
import java.util.Map;

/**
 * Buran.
 *
 * @author: ${USER} Date: 01.07.13 Time: 00:47
 */
public class BatchGet implements IDataGetter<BatchGetResult> {

    public static BatchGet c(int index, IUnfieldedDataGetter<?> getter) {
        return c(FieldIndex.c(index), getter);
    }

    public static BatchGet c(FieldIndex index, IUnfieldedDataGetter<?> getter) {
        final BatchGet batchGet = new BatchGet();
        return batchGet.add(index, getter);
    }

    private final Map<FieldIndex, IUnfieldedDataGetter<?>> entries = new HashMap<>();

    public Map<FieldIndex, IUnfieldedDataGetter<?>> getEntries() {
        return entries;
    }

    public BatchGet add(int index, IUnfieldedDataGetter<?> getter) {
        return add(FieldIndex.c(index), getter);
    }

    public BatchGet add(FieldIndex index, IUnfieldedDataGetter<?> getter) {
        this.entries.put(index, getter);
        return this;
    }
}
