package com.dcrux.buran.queryDsl;

import java.io.Serializable;

/**
 * Buran.
 *
 * @author: ${USER} Date: 06.08.13 Time: 22:23
 */
public class IndexedFieldId implements Serializable {
    private short id;

    public IndexedFieldId(short id) {
        this.id = id;
    }

    public short getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IndexedFieldId that = (IndexedFieldId) o;

        if (id != that.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (int) id;
    }

    @Override
    public String toString() {
        return "IndexedFieldId{" +
                "id=" + id +
                '}';
    }
}
