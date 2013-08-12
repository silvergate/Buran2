package com.dcrux.buran.refimpl.baseModules.textExtractor;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Buran.
 *
 * @author: ${USER} Date: 11.08.13 Time: 21:05
 */
public class Metadata {
    private final Map<MetadataTag, Object> data = new HashMap<>();

    @Nullable
    public <TJava extends Serializable> TJava get(MetadataTag<TJava> tag) {
        return (TJava) this.data.get(tag);
    }

    public boolean has(MetadataTag tag) {
        return this.data.containsKey(tag);
    }

    public <TJava extends Serializable> void put(MetadataTag<TJava> tag, TJava value) {
        this.data.put(tag, value);
    }

    public <TJava extends Serializable> void putNull(MetadataTag<TJava> tag,
            @Nullable TJava value) {
        if (value != null) {
            this.data.put(tag, value);
        }
    }

    @Override
    public String toString() {
        return "Metadata{" +
                "data=" + data +
                '}';
    }
}
