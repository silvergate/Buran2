package com.dcrux.buran.common.subscription;

import java.io.Serializable;

/**
 * Buran.
 *
 * @author: ${USER} Date: 09.08.13 Time: 18:00
 */
public class SubBlockId implements Serializable {
    private String id;

    public SubBlockId(String id) {
        this.id = id;
    }

    private SubBlockId() {
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SubBlockId that = (SubBlockId) o;

        if (!id.equals(that.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "SubBlockId{" +
                "id='" + id + '\'' +
                '}';
    }
}
