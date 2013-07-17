package com.dcrux.buran.common.domain;

import java.io.Serializable;

/**
 * Buran.
 *
 * @author: ${USER} Date: 04.07.13 Time: 14:26
 */
public final class DomainId implements Serializable {
    private long id;

    public DomainId(long id) {
        this.id = id;
    }

    private DomainId() {
    }

    public long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DomainId domainId = (DomainId) o;

        if (id != domainId.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public String toString() {
        return "DomainId{" +
                "id=" + id +
                '}';
    }
}
