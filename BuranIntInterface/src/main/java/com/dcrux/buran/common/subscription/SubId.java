package com.dcrux.buran.common.subscription;

import java.io.Serializable;

/**
 * Buran.
 *
 * @author: ${USER} Date: 17.07.13 Time: 01:15
 */
public class SubId implements Serializable {
    private int id;

    public SubId(int id) {
        this.id = id;
    }

    private SubId() {
    }

    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SubId subId = (SubId) o;

        if (id != subId.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return "SubId{" +
                "id=" + id +
                '}';
    }
}
