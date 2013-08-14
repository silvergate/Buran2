package com.dcrux.buran.common.domain;

import java.io.Serializable;

/**
 * Buran.
 *
 * @author: ${USER} Date: 13.08.13 Time: 21:40
 */
public class DomainHashId implements Serializable {
    public static final int MAX_LEN = 46;
    private String id;

    public DomainHashId(String id) {
        if (id.length() > MAX_LEN) {
            throw new IllegalArgumentException("id.length()>MAX_LEN");
        }
        this.id = id;
    }

    private DomainHashId() {
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DomainHashId that = (DomainHashId) o;

        if (!id.equals(that.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "DomainHashId{" +
                "id='" + id + '\'' +
                '}';
    }
}
