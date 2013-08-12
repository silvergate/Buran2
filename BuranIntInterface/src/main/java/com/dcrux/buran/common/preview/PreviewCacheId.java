package com.dcrux.buran.common.preview;

import java.io.Serializable;

/**
 * Buran.
 *
 * @author: ${USER} Date: 12.08.13 Time: 10:14
 */
public class PreviewCacheId implements Serializable {
    private String id;

    public PreviewCacheId(String id) {
        this.id = id;
    }

    private PreviewCacheId() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PreviewCacheId that = (PreviewCacheId) o;

        if (!id.equals(that.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "PreviewCacheId{" +
                "id='" + id + '\'' +
                '}';
    }
}
